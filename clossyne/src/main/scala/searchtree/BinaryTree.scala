package com.christophsonntag.clossyne
package searchtree

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import operations.{Delete, DeleteForward, Operation, OperationFinished, OperationForwardFinished, OperationForwardReply}

import scala.collection.immutable.Queue



class BinaryTree extends Actor with ActorLogging {
    // immutable root
    val root: ActorRef = context.actorOf(BinaryTreeNode.props("root", "root", ActorRef.noSender), "clossyneRootNode")
    var pendingOperations: Queue[Operation] = Queue.empty[Operation]

    def receive: Receive = {
        case op: Operation =>
            op match {
                case Delete(requester, key) =>
                    log.debug("Delete received: Changing context now")
                    context.become(nodeDeletion)
                    root ! DeleteForward(requester, self, key)
                case _ => root ! op
            }
        case opReply: OperationForwardFinished => opReply.destination ! OperationFinished(opReply.succeeded, None)
    }

    def nodeDeletion: Receive = {
        case op: Operation =>
            log.debug(s"Enqueuing operation ${op}")
            pendingOperations.enqueue(op)
        case opReply: OperationForwardFinished =>
            log.debug(s"Delete operation finished. Change context and send enqueued operations.")
            context.become(receive)

            opReply.destination ! OperationFinished(opReply.succeeded, None)
            pendingOperations.map(self ! _)
            pendingOperations = Queue.empty
    }
}
