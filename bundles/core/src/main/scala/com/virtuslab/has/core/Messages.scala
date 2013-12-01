package com.virtuslab.has.core

import scala.reflect.ClassTag

/**
 * @author Mikołaj Jakubowski
 */
object Messages {
  case class NewVersion[T <: VersioningActor](version: Version, actorClass: ClassTag[T])
}
