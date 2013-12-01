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

import org.osgi.framework.BundleContext

class ServiceFinder[I <: AnyRef](
    interface: Class[I],
    context: BundleContext) {

  /**
   * Applies the given function to a service. The service is found by its service interface.
   * @param f The function to be applied to a service; must not be null!
   * @return The optional result of applying the given function; None if no service available
   */
  def andApply[T](f: I => T): Option[T] = {
    context getServiceReference interface.getName match {
      case null => {
        None
      }
      case ref => {
        invokeService(ref, f, context)
      }
    }
  }

  def andApplyUnget[T](f: I => T): Option[T] = {
    context getServiceReference interface.getName match {
      case null => {
        None
      }
      case ref => {
        invokeServiceUnget(ref, f, context)
      }
    }
  }

  def andUnget = {
    context getServiceReference interface.getName match {
      case null => {
        None
      }
      case ref => {
        serviceUnget(ref, context)
      }
    }
  }

  /**
   * Applies the given function to a service and its properties. The service is found by its service interface.
   * @param f
   * @return
   */
  def andApply[T](f: (I, Props) => T): Option[T] = {
    context getServiceReference interface.getName match {
      case null => {
        None
      }
      case ref => {
        invokeServiceUnget(ref, f(_: I, ref.properties), context)
      }
    }
  }
}

private[scalamodules] class ServicesFinder[I <: AnyRef](
    interface: Class[I],
    context: BundleContext,
    filter: Option[Filter] = None) {

  /**
   * Additionally use the given Filter for finding services.
   * @param filter The Filter to be added to this ServiceFinders; must not be null!
   * @return A ServiceFinders for a service interface and the given Filter
   */
  def withFilter(filter: Filter) = {
    new ServicesFinder(interface, context, Some(filter))
  }

  /**
   * Applies the given function to all services. The services are found by service interface and an optional filter.
   * @param f The function to be applied to all services; must not be null!
   * @return A Seq with the results of applying the given function to all services
   */
  def andApply[T](f: I => T): Seq[T] = {
    context.getServiceReferences(interface.getName, filter map { _.toString } orNull) match {
      case null => {
        Nil
      }
      case refs => {
        refs.toList flatMap { invokeServiceUnget(_, f, context) }
      }
    }
  }

  /**
   * Applies the given function to all services and their properties. The services are found by service interface and an optional filter.
   * @param f The function to be applied to all services and their properties; must not be null!
   * @return A Seq with the results of applying the given function to all services
   */
  def andApply[T](f: (I, Props) => T): Seq[T] = {
    context.getServiceReferences(interface.getName, filter map { _.toString } orNull) match {
      case null => {
        Nil
      }
      case refs => {
        refs.toList flatMap { ref => invokeServiceUnget(ref, f(_: I, ref.properties), context) }
      }
    }
  }

}
