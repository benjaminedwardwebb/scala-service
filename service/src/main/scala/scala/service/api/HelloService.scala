package scala.service.api

import cats.effect.IO
import org.http4s.HttpRoutes

object HelloService:
  import org.http4s.dsl.io._

  val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO]({
    case GET -> Root / "hello" =>
      Ok("Hello, world.")
  })
