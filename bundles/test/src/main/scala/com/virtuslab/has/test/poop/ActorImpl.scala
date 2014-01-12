package com.virtuslab.has.test.poop

import com.virtuslab.has.core.{Version, VersioningActor}

/**
 * @author Mikołaj Jakubowski
 */
class SomeActorV1 extends VersioningActor {
  val version = Version(1)

  def receive = {
    case _ => println("I'm version 1")
  }
}

class SomeActorV2 extends VersioningActor {
  val version = Version(2)

  def receive = {
    case _ => println("I'm version 2!")
  }

}
