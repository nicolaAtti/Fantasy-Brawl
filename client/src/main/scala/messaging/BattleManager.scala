package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit._
import communication.MessageFormat.MyFormat
import communication._
import config.MessagingSettings
import game.{Battle, Round}
import model.{Move, Character}

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

  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = MessagingSettings.Qos) {
      consume(playerQueue) {
        body(as[StatusUpdateMessage]) { response =>
          (response.round, response.turn) match {
            case (round, turn) if round == Round.id && turn == Round.turns.head =>
              Round.updateTeamsStatuses(response.newStatuses)
              Round.endTurn()
            case _ => Unit
          }
          ack
        }
      }
    }
  }

  def updateOpponentStatus(newStatus: Move.NewStatuses, round: Int, turn: Character): Unit = {
    rabbitControl ! Message(StatusUpdateMessage(newStatus, round, turn), publisher)
  }
}
