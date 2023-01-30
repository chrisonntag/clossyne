package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

trait OperationForward {
    def requester: ActorRef
    def middleware: ActorRef
    def key: String
}
