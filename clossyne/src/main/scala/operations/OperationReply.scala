package com.christophsonntag.clossyne
package operations

sealed trait OperationReply {
    def succeeded: Boolean
    def value: Option[String]
}
case class OperationFinished(succeeded: Boolean, value: Option[String]) extends OperationReply
case class OperationResult(succeeded: Boolean, value: Option[String]) extends OperationReply
