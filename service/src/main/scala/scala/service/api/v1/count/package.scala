package scala.service.api.v1.count

import cats.effect.IO
import scala.service.TapirController

def initialize: IO[TapirController] = for {
  _ <- IO.unit
  service = CountService
  controller = CountController(service)
} yield controller
