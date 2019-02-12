package com.example.db

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Status}

import scala.collection.mutable

case class GetRequest(key: String)

case class SetRequest(key: String, value: String)

case class KeyNotFoundException(key: String) extends Exception

class DBServer extends Actor with ActorLogging {
  val map: mutable.HashMap[String, String] = mutable.HashMap()

  override def receive: Receive = {
    case GetRequest(key) => {
      log.info(s"receive GetRequest,key-$key")
      val response = map.get(key)
      response match {
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(KeyNotFoundException(key))
      }
    }
    case SetRequest(key, value) => {
      log.info(s"receiver SetRequest,key-$key,value-$value")
      map(key) = value
      sender() ! Status.Success
    }
    case _ => sender() ! Status.Failure(new ClassNotFoundException())
  }
}

object DBServer extends App {
  val system = ActorSystem("akkademy")
  system.actorOf(Props[DBServer], name = "akkademy-db")
}
