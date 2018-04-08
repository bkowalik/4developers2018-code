package fordevelopers

import java.util.UUID

import akka.stream.Materializer
import akka.stream.alpakka.cassandra.scaladsl.{ CassandraSink, CassandraSource }
import akka.stream.scaladsl.Source

import scala.language.postfixOps
import scala.util.Random

class BusinessService(
  cassandraService: CassandraService,
  kafkaService: KafkaService,
  configuration: Configuration)(implicit mat: Materializer) {

  import cassandraService.session

  def ingester(): Unit = {
    import scala.concurrent.duration._

    Source.tick(1 second, 1 second, () => UUID.randomUUID())
      .map(fun => ChatMessage(fun(), Random.alphanumeric.take(10).mkString))
      .runWith(CassandraSink(1, cassandraService.insertDummyDataPreparedStatement, ChatMessage.statementBinder))
  }

  def loader(): Unit = {
    CassandraSource(cassandraService.selectDummyDataStatement)
      .map(ChatMessage.fromRow)
      .runWith(kafkaService.produceToTopic(configuration.kafkaDummyTopic))
  }

}
