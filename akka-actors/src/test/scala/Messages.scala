/**
 * Copyright (C) 2009 Scalable Solutions.
 */

package se.scalablesolutions.akka

import akka.serialization.Serializable

sealed abstract class TestMessage
case object Ping extends TestMessage
case object Pong extends TestMessage
case object OneWay extends TestMessage
case object Die extends TestMessage
case object NotifySupervisorExit extends TestMessage

case class User(val usernamePassword: Tuple2[String, String],
                val email: String,
                val age: Int)
   extends Serializable.SBinary[User] {
  def this() = this(null, null, 0)
  import sbinary.DefaultProtocol._                                             
  implicit object UserFormat extends Format[User] {
    def reads(in : Input) = User(
      read[Tuple2[String, String]](in),
      read[String](in),
      read[Int](in))
    def writes(out: Output, value: User) = {
      write[Tuple2[String, String]](out, value.usernamePassword)
      write[String](out, value.email)
      write[Int](out, value.age)
    }
  }
  def fromBytes(bytes: Array[Byte]) = fromByteArray[User](bytes)
  def toBytes: Array[Byte] = toByteArray(this)
}

case object RemotePing extends TestMessage
case object RemotePong extends TestMessage
case object RemoteOneWay extends TestMessage
case object RemoteDie extends TestMessage
case object RemoteNotifySupervisorExit extends TestMessage
