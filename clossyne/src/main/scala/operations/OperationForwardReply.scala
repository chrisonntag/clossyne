package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

sealed trait OperationForwardReply {
    def succeeded: Boolean
    def destination: ActorRef
}
case class OperationForwardFinished(succeeded: Boolean, destination: ActorRef) extends OperationForwardReply
