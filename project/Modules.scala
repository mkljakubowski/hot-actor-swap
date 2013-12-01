import com.typesafe.sbt.osgi.SbtOsgi
import sbt._
import Process._
import Keys._

object Modules extends Build {

	val akkaVersion = "2.2.3"

	val tests = Seq(
		"org.scalatest" %% "scalatest" % "1.9.2" % "test",
		"junit" % "junit" % "4.11" % "test",
		"org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
		"com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
	)

	val akka = Seq(
		"com.typesafe.akka" %% "akka-osgi" % akkaVersion,
		"com.typesafe.akka" %% "akka-cluster" % akkaVersion
	)

	val osgi = Seq(
		"org.osgi" % "org.osgi.core" % "4.2.0" % "provided",
		"org.osgi" % "org.osgi.compendium" % "4.2.0" % "provided",
		"com.weiglewilczek.slf4s" % "slf4s_2.9.1" % "1.0.7"
	)

	val typesafe = Seq(
		Classpaths.typesafeReleases,
		Classpaths.typesafeSnapshots,
		"Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
		"Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
	)

	lazy val baseDeps = tests ++ osgi ++ akka

	val appName = "hot-actor-swap"
	val appVersion = "0.1"

	// Build definition
	lazy val buildSettings = Seq(
		organization := "com.virtuslab.has",
		version := appVersion,
		scalaVersion := "2.10.3"
	)

	// resolvers, dependencies and so on
	lazy val defaultSettings = buildSettings ++ Seq(
		resolvers ++= typesafe,
		libraryDependencies ++= baseDeps
	)

	val alsoOnTests = "compile->compile;test->test"

	lazy val scalaModules = sbt.Project(
		id = appName + "-scalamodules",
		base = file("scalamodules"),
		settings = Project.defaultSettings ++ defaultSettings ++ SbtOsgi.defaultOsgiSettings
	)

  lazy val coreBundle = sbt.Project(
    id = appName + "-core",
    base = file("bundles") / "core",
    settings = Project.defaultSettings ++ defaultSettings ++ SbtOsgi.defaultOsgiSettings
  ).aggregate(
    scalaModules
  ).dependsOn(
    scalaModules
  )

  lazy val akkaBundle = sbt.Project(
    id = appName + "-akka",
    base = file("bundles") / "akka",
    settings = Project.defaultSettings ++ defaultSettings ++ SbtOsgi.defaultOsgiSettings
  ).aggregate(
    scalaModules, coreBundle
  ).dependsOn(
    scalaModules, coreBundle
  )

  lazy val testBundle = sbt.Project(
    id = appName + "-test",
    base = file("bundles") / "test",
    settings = Project.defaultSettings ++ defaultSettings ++ SbtOsgi.defaultOsgiSettings
  ).aggregate(
    scalaModules, coreBundle, akkaBundle
  ).dependsOn(
    scalaModules, coreBundle, akkaBundle
  )

  lazy val root = sbt.Project(
		id = appName,
		base = file("."),
		settings = Project.defaultSettings ++ defaultSettings
	).aggregate(
		akkaBundle, scalaModules, testBundle
	).dependsOn(
		akkaBundle, scalaModules, testBundle
	)

}