import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import matchmaking.MongoDbManager

object Main extends App {

  final val LogMessage = "Received a new casual queue join request"
  final val LogDetailsPrefix = "Details: "
  final val Separator = "-"
  final val FailurePrint = "Failure... Caught:"
  final val OpponentNotFoundPrint = "Could not find opponent with ticket"
  final val ClientSuccessfullyRemovedPrint = "Client removed successfully"

  import communication._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = Config.Requeue)

  implicit val requestFormat: MyFormat[JoinCasualQueueRequest] = MessageFormat.format[JoinCasualQueueRequest]
  implicit val responseFormat: MyFormat[JoinCasualQueueResponse] = MessageFormat.format[JoinCasualQueueResponse]

  import Queues._
  val requestQueue = Queue(JoinCasualMatchmakingRequestQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)

  val subscription = Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = Config.Qos) {
      consume(requestQueue) {
        (body(as[JoinCasualQueueRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (Config.ServicesLog) {
              println(LogMessage)
            }
            request.operation match {
              case Config.MatchmakingAddKey =>
                MongoDbManager.getTicket.onComplete {
                  case Success(ticket: Int) =>
                    MongoDbManager.putPlayerInQueue(ticket, request.playerName, request.team, replyTo.get).onComplete {
                      case Success(_) =>
                        val opponentTicket = if (ticket % 2 == 0) ticket - 1 else ticket + 1
                        MongoDbManager.takePlayerFromQueue(opponentTicket).onComplete {
                          case Success((opponentName, opponentTeam, opponentReplyTo)) =>
                            MongoDbManager.removePlayerFromQueue(request.playerName)
                            val battleId =
                              if (ticket % 2 == 0) opponentTicket + Separator + ticket
                              else ticket + Separator + opponentTicket
                            MongoDbManager.createBattleInstance(request.playerName, opponentName, battleId).onComplete {
                              case Success(_) =>
                                sendBattleDataToBoth((request.playerName, request.team, replyTo.get),
                                                     (opponentName, opponentTeam, opponentReplyTo),
                                                     battleId)
                              case Failure(e) => println(s"$FailurePrint $e")
                            }
                          case Failure(e) => println(s"$FailurePrint $e")
                          case _          => println(s"$OpponentNotFoundPrint $opponentTicket")
                        }
                      case Failure(e) => println(s"$FailurePrint $e")
                    }
                  case Failure(e) => println(s"$FailurePrint $e")
                }
              case Config.MatchmakingRemoveKey =>
                MongoDbManager.removePlayerFromQueue(request.playerName).onComplete {
                  case Success(_)         => println(ClientSuccessfullyRemovedPrint)
                  case Failure(exception) => println(s"$FailurePrint $exception")
                }
            }
          }
          ack
        }
      }
    }
  }

  /**
    * Sends a message to the matched players
    *
    * @param dataReqPlayer the data of the first player
    * @param dataQueuedPlayer the data of the second player
    */
  def sendBattleDataToBoth(dataReqPlayer: (String, Seq[String], String),
                           dataQueuedPlayer: (String, Seq[String], String),
                           battleId: String): Unit = {
    val responseForRequester = JoinCasualQueueResponse(Right((dataQueuedPlayer._1, dataQueuedPlayer._2, battleId)))
    val responseForQueued = JoinCasualQueueResponse(Right((dataReqPlayer._1, dataReqPlayer._2, battleId)))
    rabbitControl ! Message.queue(responseForRequester, dataReqPlayer._3)
    rabbitControl ! Message.queue(responseForQueued, dataQueuedPlayer._3)
  }

}
