package com.virtuslab.has.core

import akka.actor.{ActorLogging, Actor}

/**
 * @author Mikołaj Jakubowski
 */
trait VersioningActor extends Actor with ActorLogging {
  val version : Version
}
