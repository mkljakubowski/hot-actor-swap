package com.weiglewilczek.scalamodules

/**
 * @author Mikołaj Jakubowski
 */
private[scalamodules] class AndBuilder(component: FilterComponent) {

  /**
   * Creates an And FilterComponent.
   * @param nextComponent The next FilterComponent to be "anded" with the FilterComponent of this AndBuilder; must not be null!
   * @return And FilterComponent "anding" the FilterComponent of this AndBuilder and the given one
   */
  def &&(nextComponent: FilterComponent) = and(nextComponent)

  /**
   * Creates an And FilterComponent.
   * @param nextComponent The next FilterComponent to be "anded" with the FilterComponent of this AndBuilder; must not be null!
   * @return And FilterComponent "anding" the FilterComponent of this AndBuilder and the given one
   */
  def and(nextComponent: FilterComponent) = {
    component match {
      case And(filters) => And(filters :+ Filter(nextComponent))
      case _ => And(Filter(component) :: Filter(nextComponent) :: Nil)
    }
  }
}

private[scalamodules] class OrBuilder(component: FilterComponent) {

  /**
   * Creates an Or FilterComponent.
   * @param nextComponent The next FilterComponent to be "ored" with the FilterComponent of this OrBuilder; must not be null!
   * @return Or FilterComponent "oring" the FilterComponent of this OrBuilder and the given one
   */
  def ||(nextComponent: FilterComponent) = or(nextComponent)

  /**
   * Creates an Or FilterComponent.
   * @param nextComponent The next FilterComponent to be "ored" with the FilterComponent of this OrBuilder; must not be null!
   * @return Or FilterComponent "oring" the FilterComponent of this OrBuilder and the given one
   */
  def or(nextComponent: FilterComponent) = {
    component match {
      case Or(filters) => Or(filters :+ Filter(nextComponent))
      case _ => Or(Filter(component) :: Filter(nextComponent) :: Nil)
    }
  }
}

private[scalamodules] class NotBuilder(component: FilterComponent) {

  /**
   * Creates a Not FilterComponent.
   * @return Not FilterComponent "negating" the FilterComponent of this NotBuilder
   */
  def unary_! = not

  /**
   * Creates a Not FilterComponent.
   * @return Not FilterComponent "negating" the FilterComponent of this NotBuilder
   */
  def not = Not(Filter(component))
}

private[scalamodules] class SimpleOpBuilder(attr: String) {

  /**
   * Creates a SimpleOp FilterComponent for FilterType Equal.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp initialized with the attr of this SimpleOpBuilder, a FilterType of Equal and
   * the given value
   */
  def ===(value: Any) = equal(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType Equal.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp initialized with the attr of this SimpleOpBuilder, a FilterType of Equal and
   * the given value
   */
  def equal(value: Any) = {
    require(value != null, "The value must not be null!")
    SimpleOp(attr, Equal, value)
  }

  /**
   * Creates a SimpleOp FilterComponent for FilterType Approx.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of Approx and
   * the given value
   */
  def ~==(value: Any) = approx(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType Approx.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of Approx and
   * the given value
   */
  def approx(value: Any) = {
    SimpleOp(attr, Approx, value)
  }

  /**
   * Creates a SimpleOp FilterComponent for FilterType GreaterEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of GreaterEqual and
   * the given value
   */
  def >==(value: Any) = greaterEqual(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType GreaterEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of GreaterEqual and
   * the given value
   */
  def ge(value: Any) = greaterEqual(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType GreaterEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of GreaterEqual and
   * the given value
   */
  def greaterEqual(value: Any) = {
    SimpleOp(attr, GreaterEqual, value)
  }

  /**
   * Creates a SimpleOp FilterComponent for FilterType LessEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of LessEqual and
   * the given value
   */
  def <==(value: Any) = lessEqual(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType LessEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of LessEqual and
   * the given value
   */
  def le(value: Any) = lessEqual(value)

  /**
   * Creates a SimpleOp FilterComponent for FilterType LessEqual.
   * @param value The value for the SimpleOp; must not be null!
   * @return SimpleOp inialized with the attr of this SimpleOpBuilder, a FilterType of LessEqual and
   * the given value
   */
  def lessEqual(value: Any) = {
    SimpleOp(attr, LessEqual, value)
  }
}

private[scalamodules] class PresentBuilder(attr: String) {

  /**
   * Creates a Present FilterComponent.
   * @return Present FilterComponent initialized with the attr of this PresentBuilder.
   */
  def unary_~ = present

  /**
   * Creates a Present FilterComponent.
   * @return Present FilterComponent initialized with the attr of this PresentBuilder.
   */
  def present = Present(attr)
}
