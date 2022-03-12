package scala.service.api

import cats.effect.IO
import org.http4s.{HttpRoutes, Http}
import org.http4s.server.Router

val services: HttpRoutes[IO] = HelloService.httpRoutes
val router: Http[IO, IO] = Router("/api" -> services).orNotFound
