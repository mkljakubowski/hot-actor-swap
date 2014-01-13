package com.osgidsl.scalamodules

/**
 * @author Mikołaj Jakubowski
 */
private[scalamodules] object FilterComponent {

  implicit def filterComponentToAndBuilder(filterComponent: FilterComponent) = {
    new AndBuilder(filterComponent)
  }

  implicit def filterComponentToOrBuilder(filterComponent: FilterComponent) = {
    new OrBuilder(filterComponent)
  }

  implicit def filterComponentToNotBuilder(filterComponent: FilterComponent) = {
    new NotBuilder(filterComponent)
  }
}

private[scalamodules] sealed abstract class FilterComponent

private[scalamodules] case class And(filters: List[Filter]) extends FilterComponent {
  override def toString = "&" + filters.mkString
}

private[scalamodules] case class Or(filters: List[Filter]) extends FilterComponent {
  override def toString = "|" + filters.mkString
}

private[scalamodules] case class Not(filter: Filter) extends FilterComponent {
  override def toString = "!" + filter
}

private[scalamodules] case class SimpleOp(attr: String, filterType: FilterType, value: Any) extends FilterComponent {
  override def toString = attr + filterType + value
}

private[scalamodules] case class Present(attr: String) extends FilterComponent {
  override def toString = attr + "=*"
}
