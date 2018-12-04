package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import play.api.libs.json.{Json, OFormat}
import PlayJsonSupport._
import view._
import communication._
import ViewConfiguration.viewSelector._
import com.spingo.op_rabbit.properties.ReplyTo
import javafx.application.Platform
import javafx.scene.control.Alert
import scala.concurrent.ExecutionContext.Implicits.global

/** Manages login as a guest request and response messages.
  *
  * @author Daniele Schiavi
  */
object LoginManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  import Queues._
  private val loginGuestRequestQueue = Queue(LoginGuestRequestQueue, durable = false, autoDelete = true)
  private val loginGuestResponseQueue = Queue(LoginGuestResponseQueue, durable = false, autoDelete = true)

  implicit private val RequestFormat: OFormat[LoginGuestRequest] = Json.format[LoginGuestRequest]
  implicit private val ResponseFormat: OFormat[LoginGuestResponse] = Json.format[LoginGuestResponse]

  private val publisher: Publisher = Publisher.queue(loginGuestRequestQueue)

  /** Manages login as a guest response messages. */
  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(loginGuestResponseQueue) {
        body(as[LoginGuestResponse]) { response =>
          response.guestId match {
            case Some(id) => {
              controller.TeamSelectionController.username = "guest#" + id
              ApplicationView changeView TEAM
            }
            case _ => {
              Platform runLater (() => {
                val alert: Alert = new Alert(Alert.AlertType.ERROR)
                alert setTitle "Error"
                if (response.details.isDefined)
                  alert setHeaderText response.details.get
                else
                  alert setHeaderText "Unspecified error."
                alert showAndWait ()
                ApplicationView changeView LOGIN
              })
            }
          }
          ack
        }
      }
    }
  }

  /** Send a login as a guest request message. */
  def loginAsGuestRequest(): Unit = {
    import PlayJsonSupport._
    rabbitControl ! Message(LoginGuestRequest(None), publisher, Seq(ReplyTo(loginGuestResponseQueue.queueName)))
  }
}
