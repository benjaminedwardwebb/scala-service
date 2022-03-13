package scala.service.api

import cats.effect.IO
import org.http4s.HttpRoutes
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}

object TapirService:

  private def hello(unit: Unit): IO[Either[Unit, String]] = IO.pure(Right[Unit, String]("hello, world!"))

  private val helloEndpoint: PublicEndpoint[Unit, Unit, String, Any] = 
    endpoint
      .in("tapir")
      .out(plainBody[String])

  private def countCharacters(s: String): IO[Either[Unit, Int]] = 
    IO.pure(Right[Unit, Int](s.length))

  private val countCharactersEndpoint: PublicEndpoint[String, Unit, Int, Any] = 
    endpoint
      .in("count")
      .in(stringBody)
      .out(plainBody[Int])

  val endpoints: List[AnyEndpoint] = List(
    helloEndpoint,
    countCharactersEndpoint
  )

  val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "My Bookshop", "1.0")
  val swaggerUiOptions: SwaggerUIOptions = SwaggerUIOptions.default.copy(contextPath = List("api"))

  val serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]] = List(
    helloEndpoint.serverLogic(hello),
    countCharactersEndpoint.serverLogic(countCharacters)
  ) //++: SwaggerUI[IO](docs.toYaml, swaggerUiOptions)

  val httpRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(serverEndpoints)
