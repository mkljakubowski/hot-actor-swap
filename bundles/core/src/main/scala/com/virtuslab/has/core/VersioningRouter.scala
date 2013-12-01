package com.virtuslab.has.core

import akka.actor._
import akka.routing._
import akka.dispatch.Dispatchers
import Messages.NewVersion
import scala.reflect.ClassTag

/**
 * @author Mikołaj Jakubowski
 */
case class VersioningRouter[ActorType <: VersioningActor](firstEditionClass : ClassTag[ActorType]) extends RouterConfig {

  var underlyingActors: Map[Version, ActorRef] = Map()

  def routerDispatcher: String = Dispatchers.DefaultDispatcherId

  def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy

  def newest = underlyingActors.maxBy(_._1)(VersionOrdering)._2

  def createRoute(routeeProvider: RouteeProvider): Route = {
    underlyingActors = underlyingActors updated(Version(1), routeeProvider.context.actorOf(Props()(firstEditionClass)))

    {
      case (sender, message) => {
        message match {
          case NewVersion(version, actorClass) => {
            underlyingActors = underlyingActors.updated(version, routeeProvider.context.actorOf(Props()(actorClass)))
            Nil
          }
          case _ => List(Destination(sender, newest))
        }
      }
    }
  }

}
