package scala.service.api.v1.records

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import java.util.UUID
import scala.service.TapirController
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

class RecordsController(
  service: RecordsService
) extends TapirController:

  val serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]] = List(
    RecordsController.recordsGet.serverLogic(service.list),
    RecordsController.recordsIdGet.serverLogic(service.get),
    RecordsController.recordsPost.serverLogic(service.create)
  )


object RecordsController:

  private val records: PublicEndpoint[Unit, Unit, Unit, Any] = 
    endpoint
      .in("records")

  private val recordsGet: PublicEndpoint[Unit, Unit, Seq[Record], Any] = 
    records
      .get
      .out(jsonBody[Seq[Record]])

  private val recordsIdGet: PublicEndpoint[UUID, Unit, Record, Any] = 
    records
      .in(path[UUID])
      .get
      .out(jsonBody[Record])

  private val recordsPost: PublicEndpoint[Record, Unit, Record, Any] = 
    records
      .post
      .in(jsonBody[Record])
      .out(jsonBody[Record])
