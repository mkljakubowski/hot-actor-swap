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

import org.osgi.framework.{ServiceReference, BundleContext}

private[scalamodules] trait ServiceOperations {
  def invokeService[I, T](serviceReference: ServiceReference, f: I => T, context: BundleContext): Option[T] = {
    try {
      context getService serviceReference match {
        case null => {
          None
        }
        case service => {
          val result = Some(f(service.asInstanceOf[I]))
          result
        }
      }
    }
  }

  def invokeServiceUnget[I, T](serviceReference: ServiceReference, f: I => T, context: BundleContext): Option[T] = {
    try {
      context getService serviceReference match {
        case null => {
          None
        }
        case service => {
          val result = Some(f(service.asInstanceOf[I]))
          result
        }
      }
    } finally context ungetService serviceReference
  }

  def serviceUnget[I, T](serviceReference: ServiceReference, context: BundleContext) = {
    context ungetService serviceReference
  }

}

class ServiceFinder[I <: AnyRef](interface: Class[I], context: BundleContext) extends ServiceOperations {

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

private[scalamodules] class ServicesFinder[I <: AnyRef](interface: Class[I], context: BundleContext, filter: Option[Filter] = None) extends ServiceOperations {

  def withFilter(filter: Filter) = {
    new ServicesFinder(interface, context, Some(filter))
  }

  def andApply[T](f: I => T): Seq[T] = {
    context.getServiceReferences(interface.getName, filter map {
      _.toString
    } orNull) match {
      case null => {
        Nil
      }
      case refs => {
        refs.toList flatMap {
          invokeServiceUnget(_, f, context)
        }
      }
    }
  }

  def andApply[T](f: (I, Props) => T): Seq[T] = {
    context.getServiceReferences(interface.getName, filter map {
      _.toString
    } orNull) match {
      case null => {
        Nil
      }
      case refs => {
        refs.toList flatMap {
          ref => invokeServiceUnget(ref, f(_: I, ref.properties), context)
        }
      }
    }
  }

}

