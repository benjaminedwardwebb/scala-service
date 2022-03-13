package scala.service.api.doobie

import cats.effect.IO
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import java.util.UUID
import org.http4s.{HttpRoutes, EntityEncoder}
import sttp.tapir.Schema
import sttp.tapir.json.circe._

case class Record(id: UUID, x: String, y: Int, z: Boolean)

object Record:
  implicit val encoder: Encoder[Record] = deriveEncoder[Record]
  implicit val decoder: Decoder[Record] = deriveDecoder[Record]
  implicit val schema: Schema[Record] = Schema.derived[Record]
