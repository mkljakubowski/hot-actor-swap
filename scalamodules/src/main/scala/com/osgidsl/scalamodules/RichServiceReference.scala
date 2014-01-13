package com.osgidsl.scalamodules

import org.osgi.framework.ServiceReference

private[scalamodules] class RichServiceReference(serviceReference: ServiceReference) {

  lazy val properties: Props = Map(propsFrom(serviceReference): _*)

  private def propsFrom(serviceReference: ServiceReference): Array[(String, Any)] = {
    serviceReference.getPropertyKeys match {
      case null => Array[(String, Any)]()
      case keys => keys map { key => (key, serviceReference getProperty key) }
    }
  }
}
