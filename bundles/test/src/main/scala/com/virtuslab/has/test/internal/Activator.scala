package com.virtuslab.has.test.internal

import org.osgi.framework.{BundleActivator, BundleContext}
import com.osgidsl.scalamodules._
import akka.actor._
import scala.Some
import com.virtuslab.has.test.actors.{SomeActorV2, SomeActorV1}
import scala.concurrent.duration._
import com.virtuslab.has.core.{Version, VersioningRouter, Messages}
import scala.reflect._

/**
  * @author Mikołaj Jakubowski
 */
class Activator extends BundleActivator {

  import Messages.NewVersion

  var actorSystem: Option[ServiceFinder[ActorSystem]] = None

  def stop(context: BundleContext) {
    actorSystem.map(_ andUnget)
  }

  def start(context: BundleContext) {
    actorSystem = Some(context findService withInterface[ActorSystem])

    actorSystem.map(_ andApply {
      system =>
        import system.dispatcher

        val rt = system.actorOf(Props[SomeActorV1].withRouter(VersioningRouter[SomeActorV1]))
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "1")
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "2")
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "3")
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "4")
        system.scheduler.schedule(0 milliseconds, 25 milliseconds, rt, "5")
        system.scheduler.scheduleOnce(150 milliseconds, rt, NewVersion(Version(1), classTag[SomeActorV2]))
    })
  }

}

