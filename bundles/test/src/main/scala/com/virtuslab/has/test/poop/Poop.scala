package com.virtuslab.has.test.poop

import akka.actor._
import com.virtuslab.has.core.{Version, VersioningActor}

/**
 * @author Mikołaj Jakubowski
 */
class PoopV1 extends VersioningActor {
  def version = Version(1)

  def receive = {
    case _ => println("Poop1")
  }
}

class PoopV2 extends VersioningActor {
  def version = Version(2)

  def receive = {
    case _ => println("Poop2")
  }
}
