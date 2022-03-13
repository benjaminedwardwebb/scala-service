package scala.service

import cats.effect.{IO, IOApp, ExitCode}
import org.http4s.blaze.server.BlazeServerBuilder
import scala.service.api
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends IOApp:

  def run(args: List[String]): IO[ExitCode] = for {
    _ <- initializeServer
    exitCode <- runServer
  } yield exitCode

  def initializeServer: IO[Unit] = api.initialize

  def runServer: IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withIdleTimeout(500 seconds)
      .withHttpApp(api.router)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
