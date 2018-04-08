package fordevelopers

import java.nio.file.FileSystems
import java.util.UUID

import akka.NotUsed
import akka.actor.{ ActorSystem, PoisonPill }
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.{ Flow, Sink, Source }
import akka.stream.{ Materializer, OverflowStrategy }
import fordevelopers.WsActor.{ WsConnect, WsMessage }

import scala.language.postfixOps

class WebsocketController(kafkaService: KafkaService)(implicit actorSystem: ActorSystem, mat: Materializer) extends Directives {

  def asyncWS: Flow[Message, Message, Any] = {
    val wsActor = actorSystem.actorOf(WsActor.props())

    val in: Sink[Message, NotUsed] = Flow[Message].mapAsync(1) {
      case msg: TextMessage =>
        msg.textStream.map(WsMessage).runWith(Sink.head)
      case _ =>
        Source.empty.runWith(Sink.ignore)
    }.to(Sink.actorRef(wsActor, PoisonPill))

    val out: Source[Message, NotUsed] = Source.actorRef[WsMessage](10, OverflowStrategy.fail)
      .mapMaterializedValue { outgoingActor =>
        wsActor ! WsConnect(outgoingActor)
        NotUsed
      }.map(wsMessage => TextMessage(wsMessage.text))

    Flow.fromSinkAndSource(in, out)
  }

  def kafkaWS(topic: String): Flow[Message, Message, Any] = {
    val in = Sink.ignore

    val out = kafkaService.listenTopic(topic)
      .map { consumerRecord =>
        TextMessage(consumerRecord.value())
      }

    Flow.fromSinkAndSource(in, out)
  }

  def kafkaBasedChat(topic: String): Flow[Message, Message, Any] = {
    val in = Flow[Message].mapAsync(1) {
      case msg: TextMessage =>
        msg.textStream.map(ChatMessage(UUID.randomUUID(), _: String, System.currentTimeMillis())).runWith(Sink.head)
      case _ =>
        Source.empty.runWith(Sink.head)
    }.to(kafkaService.produceToTopic[ChatMessage](topic))

    val out = kafkaService.listenTopic(topic)
      .map { consumerRecord =>
        TextMessage(consumerRecord.value())
      }

    Flow.fromSinkAndSource(in, out)
  }

  val fs = FileSystems.getDefault

  // format: OFF
  def route = get {
    (path("kafkaWS") & parameter('topic.as[String])) { topic =>
      handleWebSocketMessages(kafkaWS(topic))
    } ~
    path("asyncWS") {
      handleWebSocketMessages(asyncWS)
    } ~
    (path("chat") & parameter('channel.as[String])) { channel =>
      handleWebSocketMessages(kafkaBasedChat(channel))
    }
  }
  // format: ON

}
