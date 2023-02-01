name := "clossyne"

version := "1.0"

scalaVersion := "2.13.10"

enablePlugins(
    JavaAppPackaging,
    DockerPlugin
)

idePackagePrefix := Some("com.christophsonntag.clossyne")

Compile / mainClass := Some("com.christophsonntag.clossyne.Main")
Docker / packageName := "christophsonntag/clossyne"
dockerExposedPorts ++= Seq(4297, 4297)
dockerExposedVolumes := Seq("/tmp/clossyne")
dockerEnvVars ++= Map(("CLOSSYNE_HOST", "localhost"), ("CLOSSYNE_PORT", "4297"))

val AkkaVersion = "2.7.0"
libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
    "ch.qos.logback" % "logback-classic" % "1.2.11" % Runtime
)

resolvers ++= Seq(
    "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
)
