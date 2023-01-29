package com.christophsonntag.clossyne
package handler

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import akka.io.{IO, Tcp}
import operations._
import operations.{OperationReply, OperationFinished, GetResult}

import searchtree.BinaryTree


object MessageHandler {
    val name: String = "messageHandlerActor"
    def props(client: ActorRef, binaryTree: ActorRef) = Props(classOf[MessageHandler], client, binaryTree)
}

class MessageHandler(val client: ActorRef, val binaryTree: ActorRef) extends Actor with ActorLogging {
    import Tcp._


    def parseInput(input: String): (String, Array[String]) = {
        // All commands have length 3.
        // If arguments exist, then the fourth character is a
        // colon, after which the whitespace separated arguments start.
        val command = input.substring(0, 3)

        // Splits at the the first whitespace since all other whitespaces
        // are considered part of the value.
        // The drop method returns an empty string if input.length < 4.
        val arguments: Array[String] = input.drop(4).split("\\s", 2).takeWhile(_.nonEmpty)

        (command, arguments)
    }

    def receive: Receive = {
        case Received(data) =>
            val (command: String, arguments: Array[String]) = parseInput(data.utf8String)
            command match {
                case "EXT" => sender() ! Close; context.stop(self)
                case "SET" => binaryTree ! Set(self, arguments(0), arguments(1))
                case "GET" => binaryTree ! Get(self, arguments(0))
                case "DEL" => binaryTree ! Delete(self, arguments(0))
                case "RNG" => binaryTree ! Range(self, arguments(0), arguments(1))
            }
        case operationReply: OperationReply =>
            operationReply match {
                case OperationFinished(succeeded: Boolean, _: Option[String]) =>
                    val response: String = if(succeeded) "OK" else "NS"
                    this.client ! Write(ByteString.fromString(response))
                case GetResult(succeeded: Boolean, value: Option[String]) =>
                    val response: String = if (succeeded) value.getOrElse("NIL") else "NS"
                    this.client ! Write(ByteString.fromString(response))
            }
        case PeerClosed => context.stop(self)
    }
}
