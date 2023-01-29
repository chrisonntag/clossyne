package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

case class Set(requester: ActorRef, key: String, value: String) extends Operation
