package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

case class Range(requester: ActorRef, key: String, key2: String) extends Operation
