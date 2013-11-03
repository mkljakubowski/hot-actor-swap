package com.virtuslab.has.akka.internal

import akka.actor.ActorSystem
import org.osgi.framework.BundleContext
import akka.osgi.ActorSystemActivator

/**
 * @author Mikołaj Jakubowski
 */
class Activator extends ActorSystemActivator {

	def configure(context: BundleContext, system: ActorSystem) {
		registerService(context, system)
	}

}
