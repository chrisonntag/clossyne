package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

case class Get(requester: ActorRef, key: String) extends Operation
