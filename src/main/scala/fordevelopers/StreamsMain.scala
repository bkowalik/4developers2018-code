package fordevelopers

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object StreamsMain extends App with Module {

  val routes = wsController.route ~ fileServingController.route

  val bind = Http().bindAndHandle(
    handler = routes,
    interface = configuration.interface,
    port = configuration.port)

  //businessService.ingester()
  //businessService.loader()

  Runtime.getRuntime.addShutdownHook(new Thread { () =>
    {
      val terminateFuture = bind.map(_.unbind())
        .map(_ => actorSystem.terminate())
      Await.result(terminateFuture, 5 seconds)
    }
  })

}
