package com.virtuslab.has.test.internal

import org.osgi.framework.{BundleActivator, BundleContext}
import com.weiglewilczek.scalamodules._
import akka.actor._
import scala.Some
import com.virtuslab.has.test.poop.{PoopV2, PoopV1}
import scala.concurrent.duration._
import com.virtuslab.has.core.{VersioningRouter, Messages}
import Messages.NewVersion

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

    actorSystem.map(_ andApply {
      system =>
        import system.dispatcher

        val rt = system.actorOf(Props[PoopV1].withRouter(VersioningRouter(classOf[PoopV1])))
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "1")
        system.scheduler.scheduleOnce(150 milliseconds, rt, NewVersion(classOf[PoopV2]))
    })
  }

}

