package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import play.api.libs.json.{Json, OFormat}
import PlayJsonSupport._
import view._
import messages._
import ApplicationView.viewSelector._
import com.spingo.op_rabbit.properties.ReplyTo
import scala.concurrent.ExecutionContext.Implicits.global

object LoginManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  private val loginGuestRequestQueue = Queue(Queues.LoginGuestRequestQueue, durable = false, autoDelete = true)
  private val loginGuestResponseQueue = Queue(Queues.LoginGuestResponseQueue, durable = false, autoDelete = true)

  implicit private val RequestFormat: OFormat[LoginGuestRequest] = Json.format[LoginGuestRequest]
  implicit private val ResponseFormat: OFormat[LoginGuestResponse] = Json.format[LoginGuestResponse]

  private val publisher: Publisher = Publisher.queue(loginGuestRequestQueue)

  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(loginGuestResponseQueue) {
        body(as[LoginGuestResponse]) { response =>
          response.guestId match {
            case Some(id) => {
              println("Login as guest" + id)

              controller.TeamSelectionController.username = "guest#" + id
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
        }
      }
    }
  }

  def loginAsGuestRequest(): Unit = {
    import PlayJsonSupport._
    rabbitControl ! Message(LoginGuestRequest(None), publisher, Seq(ReplyTo(loginGuestResponseQueue.queueName)))
  }
}
