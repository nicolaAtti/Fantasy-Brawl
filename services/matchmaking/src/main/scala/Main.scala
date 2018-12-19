import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import com.spingo.op_rabbit.Directives._
import communication.MessageFormat.MyFormat
import communication.matchmaking.PlayerInfo
import config.MessagingSettings
import matchmaking.{AsyncDbManager, MongoDbManager}

/** Entry point for the service that provides matchmaking functionality,
  * pairing two consecutive clients present in the casual queue.
  *
  * @author Nicola Atti, Marco Canducci
  */
object Main extends App {

  final val LogMessage = "Received a new casual queue join request"
  final val BattleIdSeparator = "-"
  val dbManager: AsyncDbManager = MongoDbManager

  import communication._
  import config.MessagingSettings._
  import config.MiscSettings._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)
  implicit val requestFormat: MyFormat[JoinCasualQueueRequest] = MessageFormat.format[JoinCasualQueueRequest]
  implicit val responseFormat: MyFormat[JoinCasualQueueResponse] = MessageFormat.format[JoinCasualQueueResponse]

  import Queues._
  val requestQueue = Queue(JoinCasualMatchmakingRequestQueue, durable = Durable, autoDelete = AutoDelete)

  import MatchmakingHelper._

  val subscription = Subscription.run(rabbitControl) {

    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[JoinCasualQueueRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (config.MiscSettings.ServicesLog) {
              println(LogMessage)
            }

            request.operation match {
              case JoinCasualQueueRequest.Operation.ADD =>
                findAnOpponent(PlayerInfo(request.player.name, request.player.teamNames, request.player.battleQueue),
                               replyTo.get)
              case JoinCasualQueueRequest.Operation.REMOVE =>
                dbManager.notifyPlayerLeft(request.player.name)
            }
          }
          ack
        }
      }
    }
  }

  private object MatchmakingHelper {

    /** Given the information of the player that made the request, searches for
      * an opponent and sends the respective match information to both.
      *
      * @param requestInfo the information about the player who sent the actual request
      * @param requestReplyTo the matchmaking-messages response queue of the
      *                       player who made the request
      */
    def findAnOpponent(requestInfo: PlayerInfo, requestReplyTo: String): Unit = {
      dbManager.getTicket.onComplete {

        case Success(requestTicket) =>
          dbManager.putPlayerInQueue(requestTicket, requestInfo, requestReplyTo).onComplete {

            case Success(_) =>
              val opponentTicket = evaluateOpponentTicket(requestTicket)
              dbManager.takePlayerFromQueue(opponentTicket).onComplete {

                case Success(Some((opponentInfo, opponentReplyTo, opponentHasLeft))) =>
                  dbManager.removePlayerFromQueue(requestInfo.name)

                  if (opponentHasLeft) {
                    findAnOpponent(requestInfo, requestReplyTo) // try again

                  } else {
                    val battleId = evaluateBattleId(requestTicket, opponentTicket)
                    dbManager.createBattleInstance(battleId).onComplete {

                      case Success(_) =>
                        sendBattleDataToBoth(requestInfo, requestReplyTo, opponentInfo, opponentReplyTo, battleId)
                      case Failure(e) => println(s"$LogFailurePrefix$e")
                    }
                  }
                case Success(None) => Unit
                case Failure(e)    => println(s"$LogFailurePrefix$e")
              }
            case Failure(e) => println(s"$LogFailurePrefix$e")
          }
        case Failure(e) => println(s"$LogFailurePrefix$e")
      }
    }

    /** Sends a message to both the matched players.
      *
      * @param requestInfo the data of the player that made the request
      * @param requestReplyTo the queue's name for the response
      * @param opponentInfo the data of the matched opponent player
      * @param opponentReplyTo the queue's name for the opponent's response
      * @param battleId the battle identifier
      */
    def sendBattleDataToBoth(requestInfo: PlayerInfo,
                             requestReplyTo: String,
                             opponentInfo: PlayerInfo,
                             opponentReplyTo: String,
                             battleId: String): Unit = {
      val responseForRequester = JoinCasualQueueResponse(Right((opponentInfo, battleId)))
      val responseForQueued = JoinCasualQueueResponse(Right((requestInfo, battleId)))
      rabbitControl ! Message.queue(responseForRequester, requestReplyTo)
      rabbitControl ! Message.queue(responseForQueued, opponentReplyTo)
    }

    import utilities.Misc._

    /** Given a ticket, evaluates the opponent's ticket number.
      *
      * @param ticket the ticket
      * @return the opponent ticket number
      */
    def evaluateOpponentTicket(ticket: Int): Int = {
      if (isEven(ticket)) ticket - 1 else ticket + 1
    }

    /** Given two matched tickets, evaluates the battle identifier.
      *
      * @param ticket1 the first ticket number
      * @param ticket2 the second ticket number
      * @return the battle identifier for the coupled players with ticket1 and ticket2
      */
    def evaluateBattleId(ticket1: Int, ticket2: Int): String = {
      s"${ticket1 min ticket2}$BattleIdSeparator${ticket1 max ticket2}"
    }
  }

}
