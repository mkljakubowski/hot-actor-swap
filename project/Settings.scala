import sbt._
import sbt.Keys._
import sbt.Project._

/**
 * Settings for lemma project.
 *
 * @author Mikołaj Jakubowski
 */
object Settings {

  import Dependencies._

  scalacOptions += "-language:reflectiveCalls"

  lazy val ignoreIdeaFolders =
    org.sbtidea.SbtIdeaPlugin.ideaExcludeFolders := ".idea" :: ".idea_modules" :: ".settings" :: Nil

  lazy val baseDeps = tests ++ Seq(
    akka
  )

  val appName = "hot-actor-swap"
  val appVersion = "0.1"

  // Build definition
  lazy val buildSettings = Seq(
    organization := "com.virtuslab.has",
    version := appVersion,
    scalaVersion := "2.10.3",
    crossPaths := false,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )

  // resolvers, dependencies and so on
  lazy val defaultSettings = buildSettings ++ Seq(
    resolvers ++= Resolvers.typesafe,
    libraryDependencies ++= baseDeps
  )

  val alsoOnTests = "compile->compile;test->test"
}