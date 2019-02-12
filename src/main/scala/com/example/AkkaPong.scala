package com.example

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Status}


object PongActor {
  def props(resp:String):Props = Props.create(classOf[PongActor],resp)
}
class PongActor(val msg:String) extends Actor with ActorLogging{
  override def receive: Receive = {
    case "Ping" => {
      log.info(s"receive ping from (${sender()})")
    }
    case _ => sender() ! Status.Failure(new Exception("Unknown Message"))
  }
}
object AkkaPingPong extends App {
  val system = ActorSystem.create("pong")
  val pongActor1 = system.actorOf(PongActor.props("pong"),"hello")
  val ponpActor2 = system.actorSelection("akka://pong/user/hello")
  ponpActor2 ! "Ping"
  pongActor1 ! "Ping"
  pongActor1 ! "Pong"
}


