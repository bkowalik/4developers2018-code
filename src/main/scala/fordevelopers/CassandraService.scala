package fordevelopers

import com.datastax.driver.core._

class CassandraService(configuration: Configuration) {

  implicit lazy val session = Cluster.builder()
    .addContactPoint(configuration.cassandraHost)
    .withPort(configuration.cassandraPort)
    .build()
    .connect()

  val tableName = "dummy_data"

  lazy val insertDummyDataPreparedStatement: PreparedStatement = {
    session.prepare(s"INSERT INTO ${configuration.cassandraKeyspace}.$tableName (id, content, created) VALUES (?, ?, ?)")
  }

  lazy val selectDummyDataStatement: Statement = {
    val simpleStatement = new SimpleStatement(s"SELECT * FROM ${configuration.cassandraKeyspace}.$tableName")
    simpleStatement.setFetchSize(1)
  }

}
