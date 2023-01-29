package com.christophsonntag.clossyne

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.christophsonntag.clossyne.server.ClossyneServer

import java.net.{InetSocketAddress, URI}


object Main {
    def main(args: Array[String]): Unit = {
        val host = scala.util.Properties.envOrElse("CLOSSYNE_HOST", "localhost")
        val port = 4297

        val socketAddress: InetSocketAddress = new InetSocketAddress(host, port)
        val actorSystem: ActorSystem = ActorSystem.create("clossyneActorSystem")
        val clossyneServerActor: ActorRef = actorSystem.actorOf(Props(classOf[ClossyneServer], socketAddress),
            "clossyneServerActor")
    }

}
