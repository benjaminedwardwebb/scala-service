package scala.service.api.doobie

import cats.effect.IO
import java.util.UUID
import org.http4s.HttpRoutes
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.EndpointIO.annotations.endpointInput
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.circe._
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.{SwaggerUI, SwaggerUIOptions}
import com.typesafe.scalalogging.Logger

object DoobieController:

  private val logger: Logger = Logger[DoobieController.type]

  private val base: PublicEndpoint[Unit, Unit, Unit, Any] = 
    endpoint
      .in("records")

  private val list: PublicEndpoint[Unit, Unit, Seq[Record], Any] = 
    base
      .get
      .out(jsonBody[Seq[Record]])

  private val get: PublicEndpoint[UUID, Unit, Record, Any] = 
    base
      .get
      .in(path[UUID])
      .out(jsonBody[Record])

  private val post: PublicEndpoint[Record, Unit, Record, Any] = 
    base
      .post
      .in(jsonBody[Record])
      .out(jsonBody[Record])

  val endpoints: List[AnyEndpoint] = List(
    list,
    get,
    post
  )

  val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Records Service", "1.0")
  private val swaggerUiOptions: SwaggerUIOptions = SwaggerUIOptions.default.copy(
    contextPath = List("api", "records")
  )

  val serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]] = List(
    list.serverLogic(DoobieService.list),
    get.serverLogic(DoobieService.get),
    post.serverLogic(DoobieService.post)
  ) ++: SwaggerUI[IO](docs.toYaml, swaggerUiOptions)

  val httpRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(serverEndpoints)
