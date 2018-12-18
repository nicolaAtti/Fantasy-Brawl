package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import communication.MessageFormat.MyFormat
import communication.StatusUpdateMessage.CharacterKey
import communication._
import config.MessagingSettings
import controller.BattleController
import game.Round.updateTeamsStatuses
import game.{Battle, Round}
import javafx.application.Platform
import model.{Character, Status}

import scala.concurrent.ExecutionContext.Implicits.global

object BattleManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  import Queues._
  private val playerQueue =
    Queue(BattleQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  private val opponentQueue =
    Queue(Battle.opponentQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)

  implicit private val MessagesFormat: MyFormat[StatusUpdateMessage] = MessageFormat.format[StatusUpdateMessage]

  private val publisher: Publisher = Publisher.queue(opponentQueue)

  def start(): Unit = {
    Subscription.run(rabbitControl) {
      import Directives._
      channel(qos = MessagingSettings.Qos) {
        consume(playerQueue) {
          body(as[StatusUpdateMessage]) { response =>
            (response.round, response.attacker) match {
              case (round, (owner, characterName))
                  if round == Round.roundId && owner == Round.turns.head.owner.get && characterName == Round.turns.head.characterName =>
                updateTeamsStatuses(response.newStatuses)
                import BattleManagerHelper._
                Platform runLater (() => {
                  BattleController.displayMoveEffect(findCharacter(response.attacker),
                                                     response.moveName,
                                                     response.targets.map(target => findCharacter(target)))
                })
                Round.endTurn()
              case _ => Unit
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
    rabbitControl ! Message(StatusUpdateMessage(character, moveName, targets, newStatuses, round), publisher)
  }

  private object BattleManagerHelper {

    def findCharacter(characterKey: CharacterKey): Character = {
      Battle.teams
        .find(character => character.owner.get == characterKey._1 && character.characterName == characterKey._2)
        .get
    }
  }
}
