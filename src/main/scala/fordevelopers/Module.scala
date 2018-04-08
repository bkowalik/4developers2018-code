package fordevelopers

import akka.actor.ActorSystem
import akka.stream.{ ActorMaterializer, ActorMaterializerSettings, Supervision }
import com.typesafe.config.ConfigFactory

trait Module {

  val decider: Supervision.Decider = {
    case ex: Throwable =>
      ex.printStackTrace()
      Supervision.Stop
  }

  implicit val actorSystem = ActorSystem("for-developers")
  val settings = ActorMaterializerSettings(actorSystem)
    .withSupervisionStrategy(decider)
  implicit val mat = ActorMaterializer(settings)

  val config = ConfigFactory.load()
  val configuration = new Configuration(config)
  val kafkaService = new KafkaService(configuration)
  val cassandraService = new CassandraService(configuration)
  val businessService = new BusinessService(cassandraService, kafkaService, configuration)

  val wsController = new WebsocketController(kafkaService)
  val fileServingController = new FileServingController(configuration)

}
