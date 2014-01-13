package com.osgidsl.scalamodules

private[scalamodules] object Filter {
  implicit def filterComponentToFilter(filterComponent: FilterComponent) =
    Filter(filterComponent)
}

private[scalamodules] case class Filter(filterComponent: FilterComponent) {
  override def toString = "(%s)" format filterComponent
}

