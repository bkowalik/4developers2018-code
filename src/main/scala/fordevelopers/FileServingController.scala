package fordevelopers

import java.nio.file.FileSystems

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.FileIO
import akka.stream.{ Materializer, ThrottleMode }

import scala.concurrent.duration._
import scala.language.postfixOps

class FileServingController(
  configuration: Configuration)(implicit actorSystem: ActorSystem, mat: Materializer) extends Directives {

  private val fs = FileSystems.getDefault

  def downloadFileSource = FileIO.fromPath(fs.getPath(configuration.fileToDownload))

  // format: OFF
  def route = get {
    pathPrefix("file") {
      path("slow") {
        val fileSource = downloadFileSource
          .throttle(1, 50 milliseconds, 1, ThrottleMode.Shaping)
        val entity = HttpEntity(ContentTypes.`application/octet-stream`, fileSource)
        complete(entity)
      } ~
      path("normal") {
        val entity = HttpEntity(ContentTypes.`application/octet-stream`, downloadFileSource)
        complete(entity)
      }
    }
  }
  // format: ON

}
