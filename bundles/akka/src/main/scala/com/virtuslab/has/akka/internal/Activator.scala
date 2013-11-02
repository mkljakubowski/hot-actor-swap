package com.virtuslab.has.akka.internal

import org.osgi.framework.{BundleContext, BundleActivator}
import akka.actor.ActorSystem
import com.weiglewilczek.scalamodules._

/**
 * @author Mikołaj Jakubowski
 */
class Activator extends BundleActivator {

  def start(context: BundleContext) {
    context createService ActorSystem("system")
  }

  def stop(context: BundleContext) {

  }
}
