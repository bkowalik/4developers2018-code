package fordevelopers

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import fordevelopers.WsActor.{ WsConnect, WsMessage }

import scala.concurrent.duration._
import scala.language.postfixOps

object WsActor {
  sealed trait WsProtocol
  final case class WsConnect(ws: ActorRef) extends WsProtocol
  final case class WsMessage(text: String) extends WsProtocol

  def props(): Props = {
    Props(new WsActor)
  }
}

class WsActor extends Actor with ActorLogging {

  import context.dispatcher

  def receive = disconnected

  val disconnected: Receive = {
    case WsConnect(ws) =>
      context.become(connected(ws))
  }

  def connected(ws: ActorRef): Receive = {
    case WsMessage(text) =>
      log.debug(s"Message received: $text")

      context.system.scheduler.scheduleOnce(2 seconds, ws, WsMessage("Pong"))
  }
}
