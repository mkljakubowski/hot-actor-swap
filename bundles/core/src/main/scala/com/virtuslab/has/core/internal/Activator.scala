package com.virtuslab.has.core.internal

import org.osgi.framework.{BundleActivator, BundleContext}
import com.osgidsl.scalamodules._
import akka.actor._
import scala.Some

/**
 * @author Mikołaj Jakubowski
 */
class Activator extends BundleActivator {
  var actorSystem: Option[ServiceFinder[ActorSystem]] = None

  def stop(context: BundleContext) {
    actorSystem.map(_ andUnget)
  }

  def start(context: BundleContext) {
    actorSystem = Some(context findService withInterface[ActorSystem])
  }

}
