package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

case class Delete(requester: ActorRef, key: String) extends Operation
