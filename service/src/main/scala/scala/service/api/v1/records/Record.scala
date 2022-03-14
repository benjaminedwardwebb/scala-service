package scala.service.api.v1.records

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import java.util.UUID
import sttp.tapir.Schema

case class Record(id: UUID, x: String, y: Int, z: Boolean)

object Record:
  implicit val encoder: Encoder[Record] = deriveEncoder[Record]
  implicit val decoder: Decoder[Record] = deriveDecoder[Record]
  implicit val schema: Schema[Record] = Schema.derived[Record]
