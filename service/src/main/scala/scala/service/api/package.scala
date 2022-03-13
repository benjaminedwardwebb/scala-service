package scala.service.api

import cats.effect.IO
import cats.syntax.semigroupk.toSemigroupKOps
import org.http4s.server.Router
import org.http4s.{HttpRoutes, Http}

val services: HttpRoutes[IO] = HelloService.httpRoutes <+> ModelService.httpRoutes <+> TapirService.httpRoutes
val router: Http[IO, IO] = Router("/api" -> services).orNotFound
