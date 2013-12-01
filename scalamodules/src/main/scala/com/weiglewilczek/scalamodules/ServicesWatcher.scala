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

import org.osgi.framework.{ BundleContext, ServiceReference }
import org.osgi.framework.Constants._
import org.osgi.util.tracker.ServiceTracker

sealed abstract class ServiceEvent[I](service: I, properties: Props)

case class AddingService[I](service: I, properties: Props)
  extends ServiceEvent[I](service, properties)

case class ServiceModified[I](service: I, properties: Props)
  extends ServiceEvent[I](service, properties)

case class ServiceRemoved[I](service: I, properties: Props)
  extends ServiceEvent[I](service, properties)

private[scalamodules] class ServicesWatcher[I <: AnyRef](
    interface: Class[I],
    context: BundleContext,
    filter: Option[Filter] = None) {

  def withFilter(filter: Filter) = {
    new ServicesWatcher(interface, context, Some(filter))
  }

  def andHandle(handler: PartialFunction[ServiceEvent[I], Unit]) {

    val fullFilter = filter match {
      case None => Filter(OBJECTCLASS === interface.getName)
      case Some(f) => Filter(OBJECTCLASS === interface.getName && f.filterComponent)
    }

    val tracker = new ServiceTracker(context, context createFilter fullFilter.toString, null) {

      override def addingService(serviceReference: ServiceReference) = {
        val service = context getService serviceReference
        val serviceEvent = AddingService(service.asInstanceOf[I], serviceReference.properties)
        if (handler.isDefinedAt(serviceEvent)) {
          handler(serviceEvent)
        }
        service
      }

      override def modifiedService(serviceReference: ServiceReference, service: AnyRef) {
        val serviceEvent = ServiceModified(service.asInstanceOf[I], serviceReference.properties)
        if (handler.isDefinedAt(serviceEvent)) {
          handler(serviceEvent)
        }
      }

      override def removedService(serviceReference: ServiceReference, service: AnyRef) {
        val serviceEvent = ServiceRemoved(service.asInstanceOf[I], serviceReference.properties)
        if (handler.isDefinedAt(serviceEvent)) {
          handler(serviceEvent)
        }
        context ungetService serviceReference
      }
    }

    tracker.open()
  }
}
