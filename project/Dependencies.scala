import sbt._

object Dependencies {
  val http4sVersion = "1.0.0-M31"
  //val http4sVersion = "1.0-37-9661f07"
  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
}
