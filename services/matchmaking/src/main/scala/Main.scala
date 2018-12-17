import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import com.spingo.op_rabbit.Directives._
import communication.MessageFormat.MyFormat
import matchmaking.MongoDbManager

object Main extends App {

  final val LogMessage = "Received a new casual queue join request"
  final val LogDetailsPrefix = "Details: "
  final val Separator = "-"
  final val FailurePrint = "Failure... Caught:"
  final val OpponentNotFoundPrint = "Could not find opponent with ticket"
  final val ClientLeftTheGamePrint = "The player left the game"

  import communication._
  import config._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  implicit val requestFormat: MyFormat[JoinCasualQueueRequest] = MessageFormat.format[JoinCasualQueueRequest]
  implicit val responseFormat: MyFormat[JoinCasualQueueResponse] = MessageFormat.format[JoinCasualQueueResponse]

  import Queues._

  val requestQueue = Queue(JoinCasualMatchmakingRequestQueue,
    durable = MessagingSettings.Durable,
    autoDelete = MessagingSettings.AutoDelete)

  import MatchmakingHelper._

  val subscription = Subscription.run(rabbitControl) {

    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[JoinCasualQueueRequest]) & optionalProperty(ReplyTo)) {
          (request, replyTo) => {

            if (MiscSettings.ServicesLog) {
              println(LogMessage)
            }
            request.operation match {

              case MiscSettings.MatchmakingAddKey =>
                findAnOpponent(request.playerName, request.team, request.battleQueue, replyTo.get)

              case MiscSettings.MatchmakingRemoveKey =>
                MongoDbManager.notifyPlayerLeftTheGame(request.playerName).onComplete {
                  case Success(_) => println(s"${request.playerName}: $ClientLeftTheGamePrint")
                  case Failure(exception) => println(s"$FailurePrint $exception")
                }
            }
          }
            ack
        }
      }
    }
  }

  private object MatchmakingHelper {

    def findAnOpponent(playerName: String, team: Set[String], battleQueue: String, replyTo: String): Unit = {
      MongoDbManager.getTicket.onComplete {

        case Success(ticket: Int) =>
          MongoDbManager.putPlayerInQueue(ticket, playerName, team, battleQueue, replyTo).onComplete {

            case Success(_) =>
              val opponentTicket = evaluateOpponentTicket(ticket)
              MongoDbManager.takePlayerFromQueue(opponentTicket).onComplete {

                case Success((opponentName, opponentTeam, opponentBattleQueue, opponentReplyTo, opponentLeft)) =>
                  MongoDbManager.removePlayerFromQueue(playerName)

                  if (opponentLeft) {
                    findAnOpponent(playerName, team, battleQueue, replyTo) // try again

                  } else {
                    val battleId = evaluateBattleId(ticket, opponentTicket)
                    MongoDbManager.createBattleInstance(battleId).onComplete {

                      case Success(_) =>
                        println("Sending battle information to both clients ...")
                        sendBattleDataToBoth(
                          (playerName, team, replyTo),
                          (opponentName, opponentTeam, opponentReplyTo),
                          battleId)

                      case Failure(e) => println(s"$FailurePrint $e")
                    }
                  }
                case Failure(e) => println(s"$FailurePrint $e")
                case _ => println(s"$OpponentNotFoundPrint $opponentTicket")
              }
            case Failure(e) => println(s"$FailurePrint $e")
          }
        case Failure(e) => println(s"$FailurePrint $e")
      }
    }

    /** Sends a message to the matched players
      *
      * @param dataReqPlayer    the data of the first player
      * @param dataQueuedPlayer the data of the second player
      * @param battleId         the battle identifier
      */
    def sendBattleDataToBoth(dataReqPlayer: (String, Set[String], String),
                             dataQueuedPlayer: (String, Set[String], String),
                             battleId: String): Unit = {
      val responseForRequester = JoinCasualQueueResponse(
        Right((dataQueuedPlayer._1, dataQueuedPlayer._2, dataQueuedPlayer._3, battleId)))
      val responseForQueued = JoinCasualQueueResponse(
        Right((dataReqPlayer._1, dataReqPlayer._2, dataReqPlayer._3, battleId)))
      rabbitControl ! Message.queue(responseForRequester, dataReqPlayer._3)
      rabbitControl ! Message.queue(responseForQueued, dataQueuedPlayer._3)
    }

    import utilities.Misc._

    def evaluateOpponentTicket(myTicket: Int): Int = {
      if (isEven(myTicket)) myTicket - 1 else myTicket + 1
    }

    def evaluateBattleId(ticket1: Int, ticket2: Int): String = {
      s"${ticket1 min ticket2}$Separator${ticket1 max ticket2}"
    }
  }

}
