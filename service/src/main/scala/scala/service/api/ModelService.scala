package scala.service.api

import cats.effect.IO
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import java.util.UUID
import org.http4s.circe.jsonEncoderOf
import org.http4s.{HttpRoutes, EntityEncoder}

case class Model(id: UUID, a: String, b: Boolean, c: Int)

object Model:
  implicit val encoder: Encoder[Model] = deriveEncoder[Model]
  implicit val entityEncoder: EntityEncoder[IO, Model] = jsonEncoderOf(encoder)

object ModelService:

  private def getModel(id: String): IO[Model] = IO {
    val uuid = UUID.fromString(id)
    Model(uuid, "a", true, 1)
  }

  import org.http4s.dsl.io._

  val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO]({
    case GET -> Root / "model" / id => getModel(id).flatMap(Ok(_))
  })
