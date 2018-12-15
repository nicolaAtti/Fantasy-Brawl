package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import view._
import communication._
import ViewConfiguration.viewSelector._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import javafx.application.Platform
import javafx.scene.control.Alert
import scala.concurrent.ExecutionContext.Implicits.global

/** Manages login as a guest request and response messages.
  *
  * @author Daniele Schiavi
  */
object LoginManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = Config.Requeue)

  import Queues._
  private val loginGuestRequestQueue =
    Queue(LoginGuestRequestQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)
  private val loginGuestResponseQueue =
    Queue(LoginGuestResponseQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)

  implicit private val RequestFormat: MyFormat[LoginGuestRequest] = MessageFormat.format[LoginGuestRequest]
  implicit private val ResponseFormat: MyFormat[LoginGuestResponse] = MessageFormat.format[LoginGuestResponse]

  private val publisher: Publisher = Publisher.queue(loginGuestRequestQueue)

  /** Manages login as a guest response messages. */
  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = Config.Qos) {
      consume(loginGuestResponseQueue) {
        body(as[LoginGuestResponse]) { response =>
          response.guestId match {
            case Right(id) =>
              controller.TeamSelectionController.username = Config.GuestName + id
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

  /** Send a login as a guest request message. */
  def loginAsGuestRequest(): Unit = {
    rabbitControl ! Message(LoginGuestRequest(None), publisher, Seq(ReplyTo(loginGuestResponseQueue.queueName)))
  }
}
