val akkaHttpVersion = "10.1.1"
val akkaVersion = "2.5.11"


lazy val root = (project in file("."))
  .settings(
    name := "for-developers",
    name := "stream-based",
    scalaVersion := "2.12.5",
    fork := true,
    javaOptions in run ++= Seq("-Xms512m", "-Xmx512m"),
    libraryDependencies := Seq(
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.19",
      "com.lightbend.akka" %% "akka-stream-alpakka-file" % "0.18",
      "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "0.18",

      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    )
  )
