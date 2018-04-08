import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

import spray.json._

package object fordevelopers {

  implicit val jsonUUIDFormat = new RootJsonFormat[UUID] {
    override def read(json: JsValue): UUID = json match {
      case JsString(value) =>
        try {
          UUID.fromString(value)
        } catch {
          case ex: Throwable =>
            deserializationError(s"Expected UUID got: $value", ex)
        }
      case other => deserializationError(s"Expected UUID got: $other")
    }

    override def write(uuid: UUID): JsValue = JsString(uuid.toString)
  }

  implicit val instantFormat = new RootJsonFormat[Instant] {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override def read(json: JsValue): Instant = json match {
      case JsString(value) =>
        try {
          formatter.parse[Instant](value, Instant.from _)
        } catch {
          case ex: Throwable =>
            deserializationError(s"Expected UUID got: $value", ex)
        }
      case other => deserializationError(s"Expected UUID got: $other")
    }

    override def write(obj: Instant): JsValue = {
      JsString(formatter.format(obj))
    }
  }

}
