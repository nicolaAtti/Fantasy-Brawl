package main

import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import play.api.libs.json.Json

import PlayJsonSupport._

import view._

import scala.concurrent.ExecutionContext.Implicits.global

object LoginActor {
  implicit val actorSystem = ActorSystem("login")
  implicit val dataFormat = Json.format[DataLoginResponseMessage]

  val rabbitControl = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(false)

  val loginRequestQueue = Queue("loginRequest", durable = false, autoDelete = true)
  val loginResponseQueue = Queue("loginResponse", durable = false, autoDelete = true)
  val publisher = Publisher.queue(loginRequestQueue)

  val subscription = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(loginResponseQueue) {
        body(as[DataLoginResponseMessage]) { _ =>
          println("received response")
          ack
        }
      }
    }
  }
}
