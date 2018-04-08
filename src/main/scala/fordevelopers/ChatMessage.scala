package fordevelopers

import java.sql.Date
import java.util.UUID

import com.datastax.driver.core.{ BoundStatement, PreparedStatement, Row }
import spray.json.DefaultJsonProtocol._

case class ChatMessage(id: UUID, content: String, created: Long = System.currentTimeMillis())

object ChatMessage {
  implicit val format = jsonFormat3(ChatMessage.apply)

  def fromRow(row: Row): ChatMessage = {
    ChatMessage(
      id = row.getUUID("id"),
      content = row.getString("content"),
      created = row.getTimestamp("created").getTime)
  }

  def statementBinder(chatMessage: ChatMessage, preparedStatement: PreparedStatement): BoundStatement = {
    preparedStatement.bind(chatMessage.id, chatMessage.content, new Date(chatMessage.created))
  }
}
