/*
 * Copyright 2009-2011 Weigle Wilczek GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weiglewilczek.scalamodules

import org.osgi.framework.{BundleContext, ServiceRegistration}
import scala.annotation.tailrec

private[scalamodules] class RichBundleContext(context: BundleContext) {

  /**
   * Creates a service, i.e. registers one with the OSGi service registry.
   * @param service The service to be registered
   * @param properties The service properties
   * @param interface1 The optional first service interface
   * @param interface2 The optional second service interface
   * @param interface3 The optional third service interface
   */
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

  /**
   * Starting point for finding a service with the given service interface.
   * @param interface The service interface for which a ServiceFinder is to be created; must not be null!
   * @return A ServiceFinder for the given service interface
   */
  def findService[I <: AnyRef](interface: Class[I]): ServiceFinder[I] = {
    new ServiceFinder(interface, context)
  }

  /**
   * Starting point for finding all services with the given servivce interface.
   * @param interface The service interface for which a ServicesFinder is to be created; must not be null!
   * @return A ServiceFinders for the given service interface
   */
  def findServices[I <: AnyRef](interface: Class[I]): ServicesFinder[I] = {
    new ServicesFinder(interface, context)
  }

  /**
   * Starting point for watching services with the given service interface.
   * @param interface The service interface for which a ServicesWatcher is to be created; must not be null!
   * @return A ServicesWatcher for the given service interface
   */
  def watchServices[I <: AnyRef](interface: Class[I]): ServicesWatcher[I] = {
    new ServicesWatcher(interface, context)
  }
}
