package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import view._
import communication._
import ViewConfiguration.ViewSelector._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import config.MessagingSettings
import javafx.application.Platform
import javafx.scene.control.Alert
import scala.concurrent.ExecutionContext.Implicits.global

/** Manages "login as a guest" request and response messages.
  *
  * @author Daniele Schiavi
  */
object LoginManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  import Queues._
  private val loginGuestRequestQueue =
    Queue(LoginGuestRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  private val loginGuestResponseQueue =
    Queue(LoginGuestResponseQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)

  implicit private val RequestFormat: MyFormat[LoginGuestRequest] = MessageFormat.format[LoginGuestRequest]
  implicit private val ResponseFormat: MyFormat[LoginGuestResponse] = MessageFormat.format[LoginGuestResponse]

  private val publisher: Publisher = Publisher.queue(loginGuestRequestQueue)

  /** Subscribes the actor to manage "login as a guest" response messages. */
  def start(): Unit = {
    Subscription.run(rabbitControl) {
      import Directives._
      channel(qos = MessagingSettings.Qos) {
        consume(loginGuestResponseQueue) {
          body(as[LoginGuestResponse]) { response =>
            response.guestId match {
              case Right(id) =>
                controller.TeamSelectionController.username = config.MiscSettings.GuestName + id
                ApplicationView changeView TEAM
              case Left(details) =>
                Platform runLater (() => {
                  val alert: Alert = new Alert(ViewConfiguration.DialogErrorType)
                  alert setTitle ViewConfiguration.DialogErrorTitle
                  alert setHeaderText details
                  alert showAndWait ()
                  ApplicationView changeView LOGIN
                })
              case _ => Unit
            }
            ack
          }
        }
      }
    }
  }

  /** Sends a "login as a guest" request message. */
  def loginAsGuestRequest(): Unit = {
    rabbitControl ! Message(LoginGuestRequest(None), publisher, Seq(ReplyTo(loginGuestResponseQueue.queueName)))
  }
}
