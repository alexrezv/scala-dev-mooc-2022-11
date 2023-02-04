version := "0.1.0-SNAPSHOT"

scalaVersion := "3.2.1"

name := "scala3proj"

libraryDependencies := Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)