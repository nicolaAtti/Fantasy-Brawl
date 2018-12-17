package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import communication._
import config.MessagingSettings
import game.Round
import javafx.application.Platform
import javafx.scene.control.Alert
import view.ViewConfiguration.viewSelector._
import view._

import scala.concurrent.ExecutionContext.Implicits.global

object RoundManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  import Queues._
  private val startRoundRequestQueue =
    Queue(StartRoundRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  private val startRoundResponseQueue =
    Queue(StartRoundResponseQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)

  implicit private val RequestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit private val ResponseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  private val publisher: Publisher = Publisher.queue(startRoundRequestQueue)

  var currentRound: Int = 0

  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = MessagingSettings.Qos) {
      consume(startRoundResponseQueue) {
        body(as[StartRoundResponse]) { response =>
          response.turnInformation match {
            case Right(turnInformation) => {
              if (response.round > currentRound) {
                currentRound = response.round
                Round.setupTurns(turnInformation, response.round)
              } else {
                println("Received message of an old round....discarding")
              }
            }
            case Left(details) =>
              Platform runLater (() => {
                val alert: Alert = new Alert(ViewConfiguration.DialogErrorType)
                alert setTitle ViewConfiguration.DialogErrorTitle
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

  def startRoundRequest(playerName: String,
                        myTeam: Map[String, Int],
                        opponentName: String,
                        battleId: String,
                        round: Int): Unit = {
    rabbitControl ! Message(StartRoundRequest(playerName, myTeam, opponentName, battleId, round),
                            publisher,
                            Seq(ReplyTo(startRoundResponseQueue.queueName)))
  }
}
