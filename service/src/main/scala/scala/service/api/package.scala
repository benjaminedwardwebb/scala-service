package scala.service.api

import cats.effect.IO
import cats.syntax.semigroupk.toSemigroupKOps
import org.http4s.server.Router
import org.http4s.{HttpRoutes, Http}
import scala.service.api.doobie.{DoobieController, DoobieDatabase}

val initialize: IO[Unit] = DoobieDatabase.initialize
val services: HttpRoutes[IO] = HelloService.httpRoutes <+> ModelService.httpRoutes <+> TapirService.httpRoutes <+> DoobieController.httpRoutes
val router: Http[IO, IO] = Router("/api" -> services).orNotFound
