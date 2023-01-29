package com.christophsonntag.clossyne
package searchtree

import akka.actor.{Actor, ActorRef, Props}
import operations.{Operation, OperationReply}



class BinaryTree extends Actor {
    // immutable root
    val root: ActorRef = context.actorOf(Props(classOf[BinaryTreeNode], "root", "root", true), "clossyneRootNode")

    def receive: Receive = {
        case op: Operation => root ! op
        case opReply: OperationReply => context.parent ! opReply
    }
}
