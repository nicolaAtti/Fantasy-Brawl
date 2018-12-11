package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import communication._
import game.Round
import javafx.application.Platform
import javafx.scene.control.Alert
import view.ViewConfiguration.viewSelector._
import view._

import scala.concurrent.ExecutionContext.Implicits.global

object RoundManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  import Queues._
  private val startRoundRequestQueue = Queue(StartRoundRequestQueue, durable = false, autoDelete = true)
  private val startRoundResponseQueue = Queue(StartRoundResponseQueue, durable = false, autoDelete = true)

  implicit private val RequestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit private val ResponseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  private val publisher: Publisher = Publisher.queue(startRoundRequestQueue)

  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(startRoundResponseQueue) {
        body(as[StartRoundResponse]) { response =>
          response.turnInformation match {
            case Right(turnInformation) => Round.setupTurns(turnInformation)
            case Left(details) =>
              Platform runLater (() => {
                val alert: Alert = new Alert(Alert.AlertType.ERROR)
                alert setTitle "Error"
                alert setHeaderText details
                alert showAndWait ()
                ApplicationView changeView BATTLE
              })
            case _ => Unit
          }
          ack
        }
      }
    }
  }

  def startRoundRequest(playerName: String, myTeam: Map[String, Int], round: Int): Unit = {
    rabbitControl ! Message(StartRoundRequest(playerName, myTeam, round),
                            publisher,
                            Seq(ReplyTo(startRoundResponseQueue.queueName)))
  }
}
