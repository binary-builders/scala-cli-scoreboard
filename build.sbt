val scala2Version = "2.13.9"

lazy val root = project.in(file("."))
  .enablePlugins(PackPlugin)
  .settings(
    name := "scoreboard",
    version := "0.0.1",
    organization := "com.wgmouton",
    scalaVersion := scala2Version,
    packMain := Map("scoreboard" -> "com.wgmouton.scoreboard.Main"),
    resolvers += Resolver.typesafeIvyRepo("releases"),

    // Normal Scala Libraries
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.4.1",
      "org.typelevel" %% "cats-core" % "2.8.0",
      "org.typelevel" %% "cats-effect" % "3.3.14",
      "org.wvlet.airframe" %% "airframe-launcher" % "22.9.2",

      // Dependencies used for testing
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalatest" %% "scalatest" % "3.2.12" % Test
    ),
  )