package scala.service.api.v1.records

import cats.effect.IO
import scala.service.TapirController

def initialize: IO[TapirController] = for {
  database <- RecordsDatabase.initialize
  service = RecordsService(database)
  controller = RecordsController(service)
} yield controller
