package scala.service.api.v1.hello

import cats.effect.IO
import scala.service.TapirController
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint

class HelloController(
  service: HelloService
) extends TapirController:

  val serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]] = List(
    HelloController.helloGet.serverLogic(service.hello)
  ) 


object HelloController:

  private val hello: PublicEndpoint[Unit, Unit, Unit, Any] = 
    endpoint
      .in("hello")

  private val helloGet: PublicEndpoint[Unit, Unit, String, Any] = 
    hello
      .get
      .out(plainBody[String])
