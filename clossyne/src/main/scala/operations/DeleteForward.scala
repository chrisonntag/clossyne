package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

case class DeleteForward(requester: ActorRef, middleware: ActorRef, key: String) extends OperationForward
