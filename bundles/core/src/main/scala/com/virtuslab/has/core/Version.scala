package com.virtuslab.has.core

/**
 * @author Mikołaj Jakubowski
 */
case class Version(number: Int)

object VersionOrdering extends Ordering[Version] {
  def compare(a:Version, b:Version) = a.number compare b.number
}
