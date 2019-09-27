name := "CasualTrackingBE"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9",
  "com.typesafe.akka" %% "akka-stream" % "2.5.25",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "joda-time" % "joda-time" % "2.10.4",
  "org.joda" % "joda-convert" % "2.2.1",
  "mysql" % "mysql-connector-java" % "8.0.17",
  "org.slf4j" % "slf4j-nop" % "1.7.28",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "com.h2database" % "h2" % "1.4.199" % Test
)
