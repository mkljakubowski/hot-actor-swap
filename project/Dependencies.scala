import sbt._

object Dependencies {

  // Tests
  val tests = Seq(
    "org.scalatest" %% "scalatest" % "1.9.2" % "test",
    "junit" % "junit" % "4.11" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
    "com.typesafe.akka" %% "akka-testkit"  % "2.2.3" % "test"
  )

  val akka = "com.typesafe.akka" %% "akka-actor"  % "2.2.3"
  
}