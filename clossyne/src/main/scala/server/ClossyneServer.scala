package com.christophsonntag.clossyne
package server

import java.net.{InetSocketAddress, URI}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Tcp}
import com.christophsonntag.clossyne.handler.MessageHandler
import com.christophsonntag.clossyne.searchtree.BinaryTree


object ClossyneServer {
    def props(socketAddress: InetSocketAddress) = Props(classOf[ClossyneServer], socketAddress)
}

class ClossyneServer(socketAddress: InetSocketAddress) extends Actor with ActorLogging {

    import Tcp._
    import context.system

    val binaryTree: ActorRef = context.actorOf(Props[BinaryTree], "clossyneBinaryTreeActor")

    // Send Bind message with SocketAddress to the
    // manager actor, handling all low level IO resources.
    IO(Tcp)(context.system) ! Tcp.Bind(self, socketAddress)

    def receive: Receive = {
        // received Bound message, server is ready to accept incoming connections
        case b @ Tcp.Bound(localAddress: InetSocketAddress) =>
            log.debug(s"Clossyne listening on ${socketAddress.getHostName}:${socketAddress.getPort}.")
        case f @ Tcp.CommandFailed(_: Tcp.Bind) => context.stop(self)
        case c @ Tcp.Connected(remote, local) =>
            log.info(s"Client connected to ${socketAddress.getHostName}:${socketAddress.getPort}")
            log.info(s"Sender of connected message is: ${sender()}")
            sender() ! Tcp.Register(context.actorOf(Props(classOf[MessageHandler], sender(), binaryTree), "clossyneMessageHandler"))
    }

    override def postStop(): Unit = {
        IO(Tcp)(context.system) ! Tcp.Unbind
        context.stop(self)
        super.postStop()
    }
}
