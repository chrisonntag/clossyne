package com.christophsonntag.clossyne
package operations

import akka.actor.ActorRef

trait Operation {
    def requester: ActorRef
    def key: String
}
