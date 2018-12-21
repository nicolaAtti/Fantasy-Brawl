package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import communication.MessageFormat.MyFormat
import communication.StatusUpdateMessage._
import communication._
import config.MessagingSettings
import controller.BattleController
import game.Round.updateTeamsStatuses
import game.{Battle, Round}
import javafx.application.Platform
import model.{Character, Status}
import ActSelector._

import scala.concurrent.ExecutionContext.Implicits.global

object BattleManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  import Queues._
  private val playerQueue =
    Queue(BattleQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  private def opponentQueue =
    Queue(Battle.opponentQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)

  implicit private val MessagesFormat: MyFormat[StatusUpdateMessage] = MessageFormat.format[StatusUpdateMessage]

  private def publisher: Publisher = Publisher.queue(opponentQueue)

  def start(): Unit = {
    Subscription.run(rabbitControl) {
      import Directives._
      channel(qos = MessagingSettings.Qos) {
        consume(playerQueue) {
          body(as[StatusUpdateMessage]) { response =>
            import BattleManagerHelper._
            response.actSelector match {
              case SURRENDER =>
                displayMoveEffect(response)
                Battle.end(Battle.playerId)
              case UPDATE if expectedMessage(response.round, response.attacker) =>
                updateTeamsStatuses(response.newStatuses)
                displayMoveEffect(response)
                Round.endTurn()
              case SKIP if expectedMessage(response.round, response.attacker) =>
                displayMoveEffect(response)
                Round.endTurn()
            }
            ack
          }
        }
      }
    }
  }

  def updateOpponentStatus(character: CharacterKey,
                           moveName: String,
                           targets: Set[CharacterKey],
                           newStatuses: Map[CharacterKey, Status],
                           round: Int): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(character, moveName, targets, newStatuses, round, UPDATE), publisher)
  }

  def skipTurn(character: CharacterKey, round: Int): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(character, "", Set(), Map(), round, SKIP), publisher)
  }

  def surrender(): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(("", ""), "", Set(), Map(), 0, SURRENDER), publisher)
  }

  private object BattleManagerHelper {

    def findCharacter(characterKey: CharacterKey): Option[Character] = {
      Battle.teams
        .find(character => character.owner.get == characterKey._1 && character.characterName == characterKey._2)
    }

    def expectedMessage(round: Int, turn: (String, String)): Boolean = {
      round == Round.roundId && turn._1 == Round.turns.head.owner.get && turn._2 == Round.turns.head.characterName
    }

    def displayMoveEffect(response: StatusUpdateMessage): Unit = {
      Platform runLater (() => {
        BattleController.displayMoveEffect(findCharacter(response.attacker).orNull,
                                           response.moveName,
                                           response.targets.map(target => findCharacter(target).orNull),
                                           response.actSelector)
      })
    }
  }
}
