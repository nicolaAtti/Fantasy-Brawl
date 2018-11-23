package main

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import play.api.libs.json.{Json, OFormat}
import PlayJsonSupport._
import view._
import ApplicationView.viewSelector._

import scala.concurrent.ExecutionContext.Implicits.global

object LoginManager {
  implicit val actorSystem: ActorSystem = ActorSystem("login")
  implicit val dataFormat: OFormat[DataLoginResponseMessage] = Json.format[DataLoginResponseMessage]

  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  val loginRequestQueue = Queue("loginRequest", durable = false, autoDelete = true)
  val loginResponseQueue = Queue("loginResponse", durable = false, autoDelete = true)
  val publisher: Publisher = Publisher.queue(loginRequestQueue)

  val subscription: SubscriptionRef = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(loginResponseQueue) {
        body(as[DataLoginResponseMessage]) { _ =>
          println("received response")

          ApplicationView changeView TEAM

          ack
        }
      }
    }
  }
}
