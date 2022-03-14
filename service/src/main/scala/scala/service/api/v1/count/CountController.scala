package scala.service.api.v1.count

import cats.effect.IO
import scala.service.TapirController
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint

class CountController(
  service: CountService
) extends TapirController:

  val serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]] = List(
    CountController.countPost.serverLogic(service.count)
  ) 


object CountController:

  private val count: PublicEndpoint[Unit, Unit, Unit, Any] = 
    endpoint
      .in("count")

  private val countPost: PublicEndpoint[String, Unit, Int, Any] = 
    count
      .post
      .in(stringBody)
      .out(plainBody[Int])
