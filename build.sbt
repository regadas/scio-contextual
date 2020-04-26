import Dependencies._

ThisBuild / scalaVersion := "2.12.11"
ThisBuild / organization := "io.regadas"
ThisBuild / organizationName := "regadas"
ThisBuild / licenses := Seq(
  "APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / homepage := Some(url("https://github.com/regadas/scio-contextual"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/regadas/scio-contextual"),
    "scm:git@github.com:regadas/scio-contextual.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "regadas",
    name = "Filipe Regadas",
    email = "filiperegadas@gmail.com",
    url = url("https://github.com/regadas")
  )
)
ThisBuild / publishMavenStyle := true
ThisBuild / pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray)
ThisBuild / credentials += (for {
  username <- sys.env.get("SONATYPE_USERNAME")
  password <- sys.env.get("SONATYPE_PASSWORD")
} yield Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  username,
  password
))
ThisBuild / dynverSonatypeSnapshots := true
ThisBuild / publishTo := sonatypePublishToBundle.value

lazy val `scio-contextual` = project
  .in(file("."))
  .settings(
    name := "scio-contextual",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-explaintypes",
      "-unchecked",
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Xfatal-warnings",
      "-deprecation",
      "-Xlint"
    ) ++ {
      if (scalaVersion.value.startsWith("2.12")) {
        Seq("-Ypartial-unification")
      } else {
        Nil
      }
    },
    // crossScalaVersions := Seq("2.12.11", scalaVersion.value),
    libraryDependencies ++= Seq(
      Contextual,
      ScioBigQuery,
      MUnit % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    mdocOut := baseDirectory.value
  )
  .enablePlugins(MdocPlugin)
