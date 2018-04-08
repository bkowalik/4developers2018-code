package fordevelopers

import com.typesafe.config.Config

class Configuration(val underlying: Config) {

  lazy val interface = underlying.getString("for-developers.http.interface")
  lazy val port = underlying.getInt("for-developers.http.port")

  lazy val kafkaBootstrapServers = underlying.getString("for-developers.kafka.bootstrap-servers")
  lazy val kafkaDummyTopic = underlying.getString("for-developers.kafka.dummy-topic")

  lazy val cassandraHost = underlying.getString("for-developers.cassandra.host")
  lazy val cassandraPort = underlying.getInt("for-developers.cassandra.port")
  lazy val cassandraKeyspace = underlying.getString("for-developers.cassandra.keyspace")

  lazy val fileToDownload = underlying.getString("for-developers.file-to-download")

}
