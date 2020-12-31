name := "mastering-advanced-scala"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.1"

scalacOptions ++= Seq("-language:_")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.chuusai" %% "shapeless" % "2.4.0-M1",
  "co.fs2" %% "fs2-core" % "2.4.1",
  "co.fs2" %% "fs2-io" % "2.4.1",
  "com.github.julien-truffaut" %% "monocle-core" % "2.1.0",
  "com.github.julien-truffaut" %% "monocle-macro" % "2.1.0",
  "org.typelevel" %% "spire" % "0.17.0",
  "io.monix" %% "monix" % "3.3.0",
  "io.iteratee" %% "iteratee-core" % "0.19.0",
  "com.lihaoyi" %% "ammonite-ops" % "2.1.4" % "test",
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3" % "test",
  "org.specs2" %% "specs2-core" % "4.9.4" % "test",
  "org.specs2" %% "specs2-scalacheck" % "4.9.4" % "test"
) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-optics"
).map(_ % "0.13.0") ++ Seq(
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-blaze-server",
  "org.http4s" %% "http4s-circe"
).map(_ % "1.0-107-6676c1e") ++ Seq(
  "org.typelevel" %% "cats-effect",
  "org.typelevel" %% "cats-core",
  "org.typelevel" %% "cats-free"
).map(_ % "2.3.1")

// scalac options come from the sbt-tpolecat plugin so need to set any here

addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
)
