package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import properties.ReplyTo
import communication._
import MessageFormat.MyFormat
import view._
import ViewConfiguration.ViewSelector._
import config.MessagingSettings
import game.Battle
import javafx.application.Platform
import javafx.scene.control.Alert

import scala.concurrent.ExecutionContext.Implicits.global

/** Manages casual matchmaking request and response messages. */
object MatchmakingManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  import Queues._
  private val joinCasualMatchmakingRequestQueue =
    Queue(JoinCasualMatchmakingRequestQueue,
          durable = MessagingSettings.Durable,
          autoDelete = MessagingSettings.AutoDelete)
  private val joinCasualMatchmakingResponseQueue =
    Queue(JoinCasualMatchmakingResponseQueue,
          durable = MessagingSettings.Durable,
          autoDelete = MessagingSettings.AutoDelete)

  implicit private val RequestFormat: MyFormat[JoinCasualQueueRequest] = MessageFormat.format[JoinCasualQueueRequest]
  implicit private val ResponseFormat: MyFormat[JoinCasualQueueResponse] = MessageFormat.format[JoinCasualQueueResponse]

  private var myTeam: Set[String] = Set()
  private var myName: String = _

  private val publisher: Publisher = Publisher.queue(joinCasualMatchmakingRequestQueue)

  /** Manages casual matchmaking response messages. */
  def start(): Unit = {
    Subscription.run(rabbitControl) {
      import Directives._
      channel(qos = MessagingSettings.Qos) {
        consume(joinCasualMatchmakingResponseQueue) {
          body(as[JoinCasualQueueResponse]) { response =>
            response.opponentData match {
              case Right((opponentInfo, battleId)) =>
                Battle.start((myName, myTeam), opponentInfo, battleId)
              case Left(details) =>
                Platform runLater (() => {
                  val alert: Alert = new Alert(ViewConfiguration.DialogErrorType)
                  alert setTitle ViewConfiguration.DialogErrorTitle
                  alert setHeaderText details
                  alert showAndWait ()
                  ApplicationView changeView TEAM
                })
              case _ => Unit
            }
            ack
          }
        }
      }
    }
  }

  import config.MessagingSettings._

  /** Sends a casual matchmaking request message.
    *
    * @param playerName username of the player that wants to join
    * @param team team with which the player wants to fight
    */
  def joinCasualQueueRequest(playerName: String, team: Set[String]): Unit = {
    myName = playerName
    myTeam = team
    rabbitControl ! Message(
      JoinCasualQueueRequest(playerName, myTeam, PlayerJoinedCasualQueue, Queues.BattleQueue),
      publisher,
      Seq(ReplyTo(joinCasualMatchmakingResponseQueue.queueName))
    )
  }

  /** Send a message to remove the player from the casual-queue
    *
    * @param playerName username of the player that wants to be removed
    */
  def leaveCasualQueueRequest(playerName: String): Unit = {
    rabbitControl ! Message(
      JoinCasualQueueRequest(playerName, Set(), PlayerLeftCasualQueue, Queues.BattleQueue),
      publisher,
      Seq(ReplyTo(joinCasualMatchmakingResponseQueue.queueName))
    )
  }
}
