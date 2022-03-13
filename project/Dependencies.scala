import sbt._

object Dependencies {
  //val http4sVersion = "1.0.0-M31"
  val http4sVersion = "0.23.10"
  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
  val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion

  val circeVersion = "0.14.1"
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  val tapirVersion = "1.0.0-M1"
  val tapirCore = "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion
  val tapirHttp4sServer = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion
  val tapirSwaggerUi = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % tapirVersion
  val tapirOpenapiDocs = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion
  val tapirOpenapiCirceYaml = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion
}
