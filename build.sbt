name := "scala-service"
ThisBuild / organization := "benjaminedwardwebb"
ThisBuild / organizationName := "just playing around"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / scalaVersion := "3.1.0"

lazy val root = (project in file("."))
  .aggregate(service)

val commonSettings = Seq(
  libraryDependencies ++= Seq(
     Dependencies.http4sDsl,
     Dependencies.http4sBlazeServer,
     Dependencies.http4sBlazeClient,
     Dependencies.http4sCirce,
     Dependencies.circeGeneric
  )
)

val service = project
  .settings(commonSettings)
