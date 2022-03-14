package scala.service

import cats.effect.{IO, IOApp, ExitCode}

object Main extends IOApp:

  def run(args: List[String]): IO[ExitCode] = for {
    httpApp <- HttpApp.initialize
    exitCode <- HttpApp.run(httpApp)
  } yield exitCode
