package scala.service

import cats.effect.{IO, IOApp, ExitCode}
import org.http4s.blaze.server.BlazeServerBuilder
import scala.service.api

object Main extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(api.router)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
