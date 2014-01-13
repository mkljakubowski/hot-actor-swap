package com.osgidsl.scalamodules

import org.osgi.framework.{BundleContext, ServiceRegistration}
import scala.annotation.tailrec

private[scalamodules] class RichBundleContext(context: BundleContext) {

  def createService[S <: AnyRef, I1 >: S <: AnyRef, I2 >: S <: AnyRef, I3 >: S <: AnyRef](service: S,
        properties: Props = Map.empty, interface1: Option[Class[I1]] = None, interface2: Option[Class[I2]] = None,
        interface3: Option[Class[I3]] = None): ServiceRegistration = {

    val interfaces = {
      lazy val allInterfacesOrClass = {
        def allInterfaces(clazz: Class[_]) = {
          def getInterfaces(clazz: Class[_]) = clazz.getInterfaces.toList
          
          @tailrec
          def allInterfacesTR(interfaces: List[Class[_]], result: List[Class[_]]): List[Class[_]] =
            interfaces match {
              case Nil => result
              case _ => {
                val nextInterfaces = interfaces flatMap getInterfaces
                allInterfacesTR(nextInterfaces, interfaces ::: result)
              }
            }
          allInterfacesTR(getInterfaces(clazz), Nil).distinct
        }
        val interfaces = allInterfaces(service.getClass).toArray
        if (!interfaces.isEmpty) interfaces map {
          _.getName
        } else Array(service.getClass.getName)
      }
      val interfaces = List(interface1, interface2, interface3).flatten map {
        _.getName
      }
      if (!interfaces.isEmpty) interfaces.toArray else allInterfacesOrClass
    }

    val serviceRegistration =
      context.registerService(interfaces, service, if (properties.isEmpty) null else properties)
    serviceRegistration
  }

  def findService[I <: AnyRef](interface: Class[I]): ServiceFinder[I] = {
    new ServiceFinder(interface, context)
  }

  def findServices[I <: AnyRef](interface: Class[I]): ServicesFinder[I] = {
    new ServicesFinder(interface, context)
  }

  def watchServices[I <: AnyRef](interface: Class[I]): ServicesWatcher[I] = {
    new ServicesWatcher(interface, context)
  }
}
