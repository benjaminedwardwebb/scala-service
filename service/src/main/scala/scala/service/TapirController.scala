package scala.service

import cats.Monoid
import cats.effect.IO
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint

/** An interface for a "controller" defined with Tapir.
  *
  * Simply wraps a list of Tapir's [[ServerEndpoint]]s, which associates each
  * endpoint to the function implementing it in the controller's corresponding
  * service.
  *
  * Since it's just a list, this interface is also a monoid.
  */
trait TapirController:
  def serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]]


object TapirController:
  def apply(
    serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]],
  ): TapirController = {
    // Required to avoid a recursive definition a few lines below. This let's
    // is retain the trait member names as the function's parameter names.
    def newServerEndpoints = serverEndpoints
    new TapirController {
      def serverEndpoints = newServerEndpoints
    }
  }

  implicit val monoid: Monoid[TapirController] = new Monoid[TapirController] {
    def empty: TapirController = TapirController(List.empty)
    def combine(x: TapirController, y: TapirController): TapirController =
      TapirController(
        serverEndpoints = x.serverEndpoints ++ y.serverEndpoints,
      )
  }
