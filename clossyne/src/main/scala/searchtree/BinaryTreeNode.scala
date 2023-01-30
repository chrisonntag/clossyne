package com.christophsonntag.clossyne
package searchtree

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import operations._


object BinaryTreeNode {
    trait Position
    case object Left extends Position
    case object Right extends Position

    // Events that are only sent between BinaryTreeNodes
    case class DeleteChild(node: ActorRef, key: String, replyRoute: (ActorRef, ActorRef))
    case class ReplaceChild(original: ActorRef, replacement: ActorRef, key: String, replyRoute: (ActorRef, ActorRef))
    case class ReplaceParent(newParent: ActorRef)
    case class MovePredecessor(newParent: ActorRef, newSubtrees: Map[Position, ActorRef], originalActor: ActorRef, originalKey: String, replyRoute: (ActorRef, ActorRef))

    def props(k: String, v: String, parentActor: ActorRef) = Props(classOf[BinaryTreeNode], k, v, parentActor)
}

class BinaryTreeNode(val k: String, val v: String, parentActor: ActorRef) extends Actor with ActorLogging {
    import searchtree.BinaryTreeNode._

    var subtrees: Map[Position, ActorRef] = Map[Position, ActorRef]()
    var parent: ActorRef = parentActor

    val key: String = k
    val value: String = v

    def getChildFor(key: String): Position = {
        /**
         * Returns the child node depending on the lexicographic ordering of the key.
         */
        if (key > this.key) Right else Left
    }

    def receive: Receive = {
        case Set(requester, key, value) =>
            log.debug(s"Set ${key}:${value}")
            if (key != this.key) {
                val child = getChildFor(key)
                subtrees.get(child) match {
                    // Forward insert to child node if it exists, create one otherwise.
                    case Some(actor) => actor ! Set(requester, key, value)
                    case None =>
                        subtrees += (child -> context.actorOf(BinaryTreeNode.props(key, value, self), s"${key}"))
                        requester ! OperationFinished(succeeded = true, None)
                }
            } else {
                requester ! OperationFinished(succeeded = true, None)
            }

        case Get(requester, key) =>
            log.debug(s"Get ${key}. Here is ${this.key}")
            if (key != this.key) {
                val child = getChildFor(key)
                subtrees.get(child) match {
                    case Some(actor) => actor ! Get(requester, key)
                    case None => requester ! OperationResult(succeeded = false, None)
                }
            } else {
                log.info(s"Accessed pair on node ${self}:\n${key}: ${value}")
                requester ! OperationResult(succeeded = true, Some(this.value))
            }

        case DeleteChild(node, key, replyRoute) =>
            if (subtrees.values.exists(_ == node)) {
                // Check if requester is in subtree => Remove it
                val child = getChildFor(key)
                log.debug(s"Deleting the $child child")
                subtrees = subtrees.-(child)

                if (replyRoute._1 != ActorRef.noSender)
                    replyRoute._1 ! OperationForwardFinished(succeeded = true, destination = replyRoute._2)
            }

        case ReplaceChild(original, replacement, key, replyRoute) =>
            if (subtrees.values.exists(_ == original)) {
                val position = getChildFor(key)
                log.debug(s"Replacing the $position child with ${replacement}")
                subtrees = subtrees.updated(position, replacement)

                replacement ! ReplaceParent(parent)

                replyRoute._1 ! OperationForwardFinished(succeeded = true, destination = replyRoute._2)
            }

        case ReplaceParent(newParent) =>
            this.parent = newParent

        case MovePredecessor(newParent, newSubtrees, originalActor, originalKey, replyRoute) =>
            subtrees.get(Right) match {
                case Some(actor) => actor ! MovePredecessor(newParent, newSubtrees, originalActor, originalKey, replyRoute)
                case None =>
                    this.parent ! DeleteChild(self, this.key, (ActorRef.noSender, ActorRef.noSender))
                    this.parent = newParent
                    this.subtrees = newSubtrees
                    newParent ! ReplaceChild(originalActor, self, originalKey, replyRoute)
            }

        case DeleteForward(requester, middleware, key) =>
            if (key != this.key) {
                // Forward deletion message to respective child
                log.debug(s"Search for ${key}, this is ${this.key}")
                val child = getChildFor(key)
                subtrees.get(child) match {
                    case Some(actor) => actor ! DeleteForward(requester, middleware, key); log.debug("Forward to next node. ")
                    case None => middleware ! OperationForwardFinished(succeeded = false, destination = requester)
                }
            } else {
                // This is the actor that should be deleted.
                log.debug("Node for deletion found.")
                log.info(s"Deleted pair on node ${self}:\n${key}: ${value}")
                subtrees.size match {
                    case 0 =>
                        // Leaf node: Send deletion message to parent
                        log.debug("Leaf node: Send deletion to parent")
                        if (parent != ActorRef.noSender) {
                            parent ! DeleteChild(self, key, replyRoute = (middleware, requester))
                        }
                        context.stop(self)
                    case 1 =>
                        // One child: Replace position in the parent node with this actor and its whole subtree
                        val (_, actor) = subtrees.head
                        log.debug(s"One child: Replace this position in parent with ${actor}")
                        parent ! ReplaceChild(self, actor, key, replyRoute = (middleware, requester))
                    case 2 =>
                        // Replace with maximum value in left subtree, then delete this leaf.
                        subtrees.get(Left) match {
                            case Some(actor) => actor ! MovePredecessor(parent, subtrees, self, key, replyRoute = (middleware, requester))
                            case None => middleware ! OperationForwardFinished(succeeded = false, destination = requester)
                        }
                }
            }
    }
}
