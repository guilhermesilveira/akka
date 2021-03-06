package se.scalablesolutions.akka.actor

import java.util.concurrent.TimeUnit

import org.scalatest.junit.JUnitSuite
import org.junit.Test

import se.scalablesolutions.akka.dispatch.Dispatchers

class ThreadBasedActorTest extends JUnitSuite {
  import Actor.Sender.Self

  private val unit = TimeUnit.MILLISECONDS

  class TestActor extends Actor {
    dispatcher = Dispatchers.newThreadBasedDispatcher(this)
    
    def receive = {
      case "Hello" =>
        reply("World")
      case "Failure" =>
        throw new RuntimeException("expected")
    }
  }
  
  @Test def shouldSendOneWay = {
    implicit val timeout = 5000L
    var oneWay = "nada"
    val actor = new Actor {
      def receive = {
        case "OneWay" => oneWay = "received"
      }
    }
    actor.start
    val result = actor ! "OneWay"
    Thread.sleep(100)
    assert("received" === oneWay)
    actor.stop
  }

  @Test def shouldSendReplySync = {
    implicit val timeout = 5000L
    val actor = new TestActor
    actor.start
    val result: String = (actor !! ("Hello", 10000)).get
    assert("World" === result)
    actor.stop
  }

  @Test def shouldSendReplyAsync = {
    implicit val timeout = 5000L
    val actor = new TestActor
    actor.start
    val result = actor !! "Hello"
    assert("World" === result.get.asInstanceOf[String])
    actor.stop
  }

  @Test def shouldSendReceiveException = {
    implicit val timeout = 5000L
    val actor = new TestActor
    actor.start
    try {
      actor !! "Failure"
      fail("Should have thrown an exception")
    } catch {
      case e =>
        assert("expected" === e.getMessage())
    }
    actor.stop
  }
}
