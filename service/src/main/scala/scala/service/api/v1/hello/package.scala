package scala.service.api.v1.hello

import cats.effect.IO
import scala.service.TapirController

def initialize: IO[TapirController] = for {
  _ <- IO.unit
  service = HelloService
  controller = HelloController(service)
} yield controller
