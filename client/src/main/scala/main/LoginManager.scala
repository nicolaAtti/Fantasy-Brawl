package main

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import play.api.libs.json.{Json, OFormat}
import PlayJsonSupport._
import view._
import messages._
import ApplicationView.viewSelector._

import scala.concurrent.ExecutionContext.Implicits.global

object LoginManager {
  implicit val actorSystem: ActorSystem = ActorSystem("login")

  val rabbitControl: ActorRef = actorSystem.actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  val loginGuestRequestQueue = Queue(Queues.LoginGuestRequestQueue, durable = false, autoDelete = true)
  val loginGuestResponseQueue = Queue(Queues.LoginGuestResponseQueue, durable = false, autoDelete = true)
  val publisher: Publisher = Publisher.queue(loginGuestRequestQueue)

  implicit val dataFormat: OFormat[LoginGuestResponse] = Json.format[LoginGuestResponse]
  val subscription: SubscriptionRef = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(loginGuestResponseQueue) {
        body(as[LoginGuestResponse]) { response =>
          response.guestId match {
            case Some(id) => {
              println("Login as guest" + id)
              ApplicationView changeView TEAM
            }
            case _ => println("Login error")
          }
          response.details match {
            case Some(details) => {
              println(details)
            }
            case _ =>
          }
          ack
          /*case LoginGuestResponse(Some(id), None) => {
            println("Login as guest" + id)
            ApplicationView changeView TEAM
            ack
          }
          case LoginGuestResponse(None, Some(details)) => {
            println("ERROR: " + details)
            ack
          }
          case _ => {
            println("Message syntax error: accepted only (id,None) or (None,error).")
            ack
          }*/
        }
      }
    }
  }
}
