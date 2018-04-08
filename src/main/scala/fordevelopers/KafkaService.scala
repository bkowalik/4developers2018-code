package fordevelopers

import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{ Consumer, Producer }
import akka.kafka.{ ConsumerSettings, ProducerSettings, Subscriptions }
import akka.stream.Materializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import org.apache.kafka.clients.consumer.{ ConsumerConfig, ConsumerRecord }
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ StringDeserializer, StringSerializer }
import spray.json._

class KafkaService(
  configuration: Configuration)(implicit actorSystem: ActorSystem, mat: Materializer) {

  val consumerSettings: ConsumerSettings[String, String] = {
    ConsumerSettings(actorSystem, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(configuration.kafkaBootstrapServers)
      .withGroupId(UUID.randomUUID.toString)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
  }

  def listenTopic(topic: String): Source[ConsumerRecord[String, String], Consumer.Control] = {
    Consumer.plainSource(consumerSettings, Subscriptions.topics(topic))
  }

  val producerSettings: ProducerSettings[String, String] = {
    ProducerSettings(actorSystem, new StringSerializer, new StringSerializer)
      .withBootstrapServers(configuration.kafkaBootstrapServers)
  }

  def produceToTopic[A: JsonWriter](topic: String): Sink[A, NotUsed] = {
    Flow[A].map { payload =>
      new ProducerRecord[String, String](topic, payload.toJson.compactPrint)
    }.to(Producer.plainSink(producerSettings))
  }

}
