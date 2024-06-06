version := "0.1"

scalaVersion := "3.3.3"

name := "dispatcher_scala"

dockerBaseImage := "eclipse-temurin:17"
dockerExposedPorts += 8080

enablePlugins(
  JavaAppPackaging,
  DockerPlugin
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
  "com.typesafe.akka" %% "akka-stream" % "2.8.5",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "org.slf4j" % "slf4j-api" % "2.0.12"
)

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

