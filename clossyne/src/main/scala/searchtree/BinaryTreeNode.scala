package com.christophsonntag.clossyne
package searchtree

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import operations._


object BinaryTreeNode {
    trait Position
    case object Left extends Position
    case object Right extends Position

    def props(key: String, value: String, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode], key, value, initiallyRemoved)
}

class BinaryTreeNode(val key: String, val value: String, initiallyRemoved: Boolean) extends Actor with ActorLogging {
    import searchtree.BinaryTreeNode._

    var subtrees = Map[Position, ActorRef]()
    var removed = initiallyRemoved

    def getChildFor(key: String): Position = {
        /**
         * Returns the child node depending on the lexicographic ordering of the key.
         */
        if (key > this.key) Right else Left
    }

    def receive: Receive = {
        case Set(requester, key, value) =>
            log.debug(s"Set ${key}:${value} as requested by ${requester}")
            if (key != this.key || (key == this.key && this.removed)) {
                val child = getChildFor(key)
                subtrees.get(child) match {
                    // Forward insert to child node if it exist, create one otherwise.
                    case Some(actor) => actor ! Set(requester, key, value)
                    case None =>
                        subtrees += (child -> context.actorOf(Props(classOf[BinaryTreeNode], key, value, false)))
                        requester ! OperationFinished(succeeded = true, None)
                }
            } else {
                requester ! OperationFinished(succeeded = true, None)
            }

        case Get(requester, key) =>
            if (key != this.key || (key == this.key && this.removed)) {
                val child = getChildFor(key)
                subtrees.get(child) match {
                    case Some(actor) => actor ! Get(requester, key)
                    case None => requester ! GetResult(succeeded = false, None)
                }
            } else {
                requester ! GetResult(succeeded = true, Some(this.value))
            }
    }
}
