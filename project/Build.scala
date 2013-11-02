import sbt.Keys._
import sbt._

object Modules extends Build {

  import Settings._

  lazy val scalaModules = sbt.Project(
    id = appName + "-scalamodules",
    base = file("scalamodules"),
    settings = defaultSettings
  )

  lazy val akkaBundle = sbt.Project(
    id = appName + "-akka",
    base = file("bundles") / "akka",
    settings = defaultSettings
  ).aggregate(
    scalaModules
  ).dependsOn(
    scalaModules
  )
  
}