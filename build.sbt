name := """AkkaRemoteAskTest"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	jdbc,
	cache,
	ws,
	"com.typesafe.akka" %% "akka-remote" % "2.3.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"