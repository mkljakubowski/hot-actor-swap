package com.osgidsl.scalamodules

/**
 * @author Mikołaj Jakubowski
 */
private[scalamodules] sealed abstract class FilterType

private[scalamodules] case object Equal extends FilterType {
  override def toString = "="
}

private[scalamodules] case object Approx extends FilterType {
  override def toString = "~="
}

private[scalamodules] case object GreaterEqual extends FilterType {
  override def toString = ">="
}

private[scalamodules] case object LessEqual extends FilterType {
  override def toString = "<="
}

