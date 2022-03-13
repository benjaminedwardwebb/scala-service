name := "scala-service"
ThisBuild / organization := "benjaminedwardwebb"
ThisBuild / organizationName := "just playing around"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / scalaVersion := "3.1.0"

lazy val root = service

val service = project
  .settings(libraryDependencies ++= Seq(
     Dependencies.http4sDsl,
     Dependencies.http4sBlazeServer,
     Dependencies.http4sBlazeClient,
     Dependencies.http4sCirce,
     Dependencies.circeGeneric,
     Dependencies.tapirCore,
     Dependencies.tapirHttp4sServer,
     Dependencies.tapirSwaggerUi,
     Dependencies.tapirOpenapiDocs,
     Dependencies.tapirOpenapiCirceYaml,
     Dependencies.tapirJsonCirce,
     Dependencies.doobieCore,
     Dependencies.doobieH2,
     Dependencies.scalaLogging,
     Dependencies.logbackClassic,
     Dependencies.h2
  ))
