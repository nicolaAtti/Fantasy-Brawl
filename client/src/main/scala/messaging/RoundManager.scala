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
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = Config.Requeue)

  import Queues._
  private val startRoundRequestQueue =
    Queue(StartRoundRequestQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)
  private val startRoundResponseQueue =
    Queue(StartRoundResponseQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)

  implicit private val RequestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit private val ResponseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  private val publisher: Publisher = Publisher.queue(startRoundRequestQueue)

  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = Config.Qos) {
      consume(startRoundResponseQueue) {
        body(as[StartRoundResponse]) { response =>
          response.turnInformation match {
            case Right(turnInformation) => Round.setupTurns(turnInformation, response.round)
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
