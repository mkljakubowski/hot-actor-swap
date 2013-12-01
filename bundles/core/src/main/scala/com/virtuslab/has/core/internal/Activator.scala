package com.virtuslab.has.core.internal

import org.osgi.framework.{BundleActivator, BundleContext}
import com.weiglewilczek.scalamodules._
import akka.actor._
import akka.routing._
import akka.dispatch._
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

    actorSystem.map(_ andApply {
      system =>
        system.actorOf(Props[Poop].withRouter(VersioningRouter(classOf[Poop])))
    })
  }

}

case class Version(number: Int)

object VersionOrdering extends Ordering[Version] {
  def compare(a:Version, b:Version) = a.number compare b.number
}

trait VersioningActor extends Actor with ActorLogging {
  def version:Version
}

class Poop extends VersioningActor {
  def receive = {
    case _ => println("Poop")
  }

  def version = Version(1)
}

case class VersioningRouter[ActorType <: VersioningActor](firstEditionClass : Class[ActorType]) extends RouterConfig {

  var underlyingActors: Map[Version, ActorRef] = Map()

  def routerDispatcher: String = Dispatchers.DefaultDispatcherId

  def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy

  def newest = underlyingActors.maxBy(_._1)(VersionOrdering)._2

  def createRoute(routeeProvider: RouteeProvider): Route = {
    underlyingActors = underlyingActors updated(Version(1), routeeProvider.context.actorOf(Props(firstEditionClass, "1")))

    {
      case (sender, message) => {
        message match {
          case _ => List(Destination(sender, newest))
        }
      }
    }
  }
}
