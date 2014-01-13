package com.osgidsl.scalamodules

/**
 * @author Mikołaj Jakubowski
 */
private[scalamodules] class AndBuilder(component: FilterComponent) {

  def &&(nextComponent: FilterComponent) = and(nextComponent)

  def and(nextComponent: FilterComponent) = {
    component match {
      case And(filters) => And(filters :+ Filter(nextComponent))
      case _ => And(Filter(component) :: Filter(nextComponent) :: Nil)
    }
  }
}

private[scalamodules] class OrBuilder(component: FilterComponent) {

  def ||(nextComponent: FilterComponent) = or(nextComponent)

  def or(nextComponent: FilterComponent) = {
    component match {
      case Or(filters) => Or(filters :+ Filter(nextComponent))
      case _ => Or(Filter(component) :: Filter(nextComponent) :: Nil)
    }
  }
}

private[scalamodules] class NotBuilder(component: FilterComponent) {

  def unary_! = not

  def not = Not(Filter(component))
}

private[scalamodules] class SimpleOpBuilder(attr: String) {

  def ===(value: Any) = equal(value)

  def equal(value: Any) = SimpleOp(attr, Equal, value)

  def ~==(value: Any) = approx(value)

  def approx(value: Any) = SimpleOp(attr, Approx, value)

  def >==(value: Any) = greaterEqual(value)

  def ge(value: Any) = greaterEqual(value)

  def greaterEqual(value: Any) = SimpleOp(attr, GreaterEqual, value)

  def <==(value: Any) = lessEqual(value)

  def le(value: Any) = lessEqual(value)

  def lessEqual(value: Any) = SimpleOp(attr, LessEqual, value)
}

private[scalamodules] class PresentBuilder(attr: String) {

  def unary_~ = present

  def present = Present(attr)
}
