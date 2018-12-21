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

/** Manages the battle messages.
  *
  * @author Daniele Schiavi
  */
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

  /** Subscribes the actor to manage the opponent's messages. */
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

  /** Sends an update message to the opponent to notify him about the move made.
    *
    * @param character the character who made the move
    * @param moveName the name of the move
    * @param targets the targets of the move
    * @param newStatuses the characters' new statuses after the move is made
    * @param round the current round
    */
  def updateOpponentStatus(character: CharacterKey,
                           moveName: String,
                           targets: Set[CharacterKey],
                           newStatuses: Map[CharacterKey, Status],
                           round: Int): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(character, moveName, targets, newStatuses, round, UPDATE), publisher)
  }

  /** Sends a message to the opponent notifying him that the turn has been skipped.
    *
    * @param character the character who skips the turn
    * @param round the current round
    */
  def skipTurn(character: CharacterKey, round: Int): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(character, "", Set(), Map(), round, SKIP), publisher)
  }

  /** Sends a surrender message to the opponent in order to notify him. */
  def surrender(): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(("", ""), "", Set(), Map(), 0, SURRENDER), publisher)
  }

  private object BattleManagerHelper {

    /** Given the owner and the name of a character, returns the corresponding
      * character object (if present).
      *
      * @param characterKey the owner and the name of the character
      * @return an optional character if present in one team
      */
    def findCharacter(characterKey: CharacterKey): Option[Character] = {
      Battle.teams
        .find(character => character.owner.get == characterKey._1 && character.characterName == characterKey._2)
    }

    /** Checks if the given round and turn coincide with the current one.
      *
      * @param round the round to check
      * @param turn the owner and name of character to check
      * @return true if coincide false if else
      */
    def expectedMessage(round: Int, turn: (String, String)): Boolean = {
      round == Round.roundId && turn._1 == Round.turns.head.owner.get && turn._2 == Round.turns.head.characterName
    }

    /** Given an opponent's message display the effect of the move in the GUI.
      *
      * @param response the opponent's message
      */
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
