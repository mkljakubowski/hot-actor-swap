package com.virtuslab.has.core

import com.virtuslab.has.core.VersioningActor

/**
 * @author Mikołaj Jakubowski
 */
object Messages {
  case class NewVersion[T <: VersioningActor](actorClass: Class[T])

}
