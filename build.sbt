import sbt.Keys.{libraryDependencies}

name := "quaich-other-samples"

val projectVersion        = "0.0.4-SNAPSHOT"

parallelExecution in ThisBuild := false

lazy val commonSettings = Seq(
  organization := "codes.bytes",
  version := projectVersion,
  scalaVersion := "2.12.2",
  retrieveManaged := true,

  libraryDependencies ++= Seq(
    "codes.bytes" %% "quaich-http" % projectVersion,

    "ch.qos.logback" % "logback-classic" % "1.1.7" % Test,
    "org.scalatest" %% "scalatest" % "3.0.0" % Test,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" % Test
  ),
  scalacOptions := Seq(
    "-encoding",
    "UTF-8",
    "-target:jvm-1.8",
    "-deprecation",
    "-language:_"
  ),
  fork in (Test, run) := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "quaich-other-samples",
    version := projectVersion
  ).
  aggregate(
    util, plainDemo, akkaDemo
  )

lazy val util = (project in file("util")).
  settings(commonSettings: _*).
  settings(
    name := "util",
    libraryDependencies ++= Seq(
    )
  )

lazy val plainDemo = (project in file("plain-demo")).
  settings(commonSettings: _*).
  settings(quaichMacroSettings: _*).
  settings(
    name := "plain-demo",
    createAutomatically := true,

    awsLambdaMemory := 192,
    awsLambdaTimeout := 30,
    region := "eu-west-1",

    publishArtifact in (Compile, packageDoc) := false,

    libraryDependencies ++= Seq(
      // Lambda client libs
      "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.123"
    )
  ).
  dependsOn(util).
  enablePlugins(AWSLambdaPlugin, AwsApiGatewayPlugin)

lazy val akkaDemo = (project in file("akka-demo")).
  settings(commonSettings: _*).
  settings(quaichMacroSettings: _*).
  settings(
    name := "akka-demo",
    createAutomatically := true,

    awsLambdaMemory := 192,
    awsLambdaTimeout := 30,
    region := "eu-west-1",

    publishArtifact in (Compile, packageDoc) := false,

    libraryDependencies ++= Seq(
      // Lambda client libs
      "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.123",

      "com.typesafe.akka" %% "akka-typed" % "2.5.2"
    )
  ).
  dependsOn(util).
  enablePlugins(AWSLambdaPlugin, AwsApiGatewayPlugin)



