package scala.service

import cats.effect.{IO, ExitCode}
import cats.syntax.all.catsSyntaxSemigroup
import cats.syntax.semigroupk.toSemigroupKOps
import com.typesafe.scalalogging.Logger
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.{HttpRoutes, Http}
import scala.concurrent.duration._
import scala.language.postfixOps
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}

object HttpApp:

  def initialize: IO[Http[IO, IO]] = for {
    hello <- api.v1.hello.initialize
    count <- api.v1.count.initialize
    records <- api.v1.records.initialize
    _ = logger.info("Initialized all controllers.")
    controllers = hello combine count combine records
    // The Swagger UI is a sort of "virtual" controller here. It depends on
    // all others because it documents their endpoints.
    controllersWithSwaggerUi = controllers combine getSwaggerUiController(controllers)
    _ = logger.info("Combined controllers and initialized OpenAPI Swagger UI.")
    httpApp <- getHttpAppRouter(controllersWithSwaggerUi)
    _ = logger.info("Initialized HTTP application router.")
  } yield httpApp

  def run(httpApp: Http[IO, IO]): IO[ExitCode] = {
    logger.info("Running server ...")
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withIdleTimeout(500 seconds)
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

  private val version = "1.0"
  private val majorVersion = version.split('.').head
  private val base = List("api", s"v${majorVersion}")
  private val basePath = base.mkString("/")
  private val name = "Example Scala Service API"

  private val logger: Logger = Logger[HttpApp.type]

  private def getHttpAppRouter(controller: TapirController): IO[Http[IO, IO]] = IO.blocking({
    val serverEndpoints = controller.serverEndpoints
    val httpRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(serverEndpoints)
    val router: Http[IO, IO] = Router(basePath -> httpRoutes).orNotFound 
    router
  })

  private def getSwaggerUiController(controller: TapirController): TapirController = {
    logger.info("Initializing OpenAPI Swagger UI controller ...")
    import sttp.tapir.AnyEndpoint
    val endpoints: List[AnyEndpoint] = controller.serverEndpoints.map(_.endpoint)
    val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(endpoints, name, version)
    val swaggerUiOptions: SwaggerUIOptions = SwaggerUIOptions.default.copy(contextPath = base)
    val swaggerUi: List[ServerEndpoint[Fs2Streams[IO], IO]] = SwaggerUI[IO](docs.toYaml, swaggerUiOptions)
    TapirController(swaggerUi)
  }
