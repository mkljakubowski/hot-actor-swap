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
package com.weiglewilczek

import java.util.Dictionary
import org.osgi.framework.{ BundleContext, ServiceReference }

/**
 * Some implicit conversions and other stuff essential for the ScalaModules DSL.
 */
package object scalamodules {

  type Props = Map[String, Any]

  implicit def toRichBundleContext(context: BundleContext): RichBundleContext = {
    new RichBundleContext(context)
  }

  implicit def toRichServiceReference(serviceReference: ServiceReference): RichServiceReference = {
    new RichServiceReference(serviceReference)
  }

  implicit def pairToMap[A, B](pair: (A, B)): Map[A, B] =
    if (pair == null) null else Map(pair)

  implicit def toSimpleOpBuilder(attr: String): SimpleOpBuilder = {
    new SimpleOpBuilder(attr)
  }

  implicit def toPresentBuilder(attr: String): PresentBuilder = {
    new PresentBuilder(attr)
  }

  def interface[I](implicit manifest: Manifest[I]): Option[Class[I]] =
    Some(manifest.runtimeClass.asInstanceOf[Class[I]])

  def withInterface[I](implicit manifest: Manifest[I]): Class[I] =
    manifest.runtimeClass.asInstanceOf[Class[I]]

  private[scalamodules] implicit def scalaMapToJavaDictionary[K, V](map: Map[K, V]) = {
    import scala.collection.JavaConversions._
    if (map == null) null: Dictionary[K, V]
    else new Dictionary[K, V] {
      override def size = map.size
      override def isEmpty = map.isEmpty
      override def keys = map.keysIterator
      override def elements = map.valuesIterator
      override def get(o: Object) = map.get(o.asInstanceOf[K]) match {
        case None => null.asInstanceOf[V]
        case Some(value) => value.asInstanceOf[V]
      }
      override def put(key: K, value: V) =
        throw new UnsupportedOperationException("This Dictionary is read-only!")
      override def remove(o: Object) =
        throw new UnsupportedOperationException("This Dictionary is read-only!")
    }
  }


}
