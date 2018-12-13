import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo

object Main extends App {

  final val Log = true
  final val LogMessage = "Received a new casual queue join request"
  final val LogDetailsPrefix = "Details: "

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(requeue = false)

  import communication._
  implicit val requestFormat = MessageFormat.format[JoinCasualQueueRequest]
  implicit val responseFormat = MessageFormat.format[JoinCasualQueueResponse]

  import Queues._
  val requestQueue = Queue(JoinCasualMatchmakingRequestQueue, durable = false, autoDelete = true)

  val subscription = Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = 3) {
      consume(requestQueue) {
        (body(as[JoinCasualQueueRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (Log) {
              println(LogMessage)
            }
            request.operation match {
              case "Add" =>
                AsyncDbManager.getTicket.onComplete {
                  case Success(ticket: Int) =>
                    AsyncDbManager.putPlayerInQueue(ticket, request.playerName, request.team, replyTo.get).onComplete {
                      case Success(_) => {
                        val opponentTicket = if (ticket % 2 == 0) ticket - 1 else ticket + 1
                        AsyncDbManager.takePlayerFromQueue(opponentTicket).onComplete {
                          case Success((opponentName, opponentTeam, opponentReplyTo)) => {
                            AsyncDbManager.removePlayerFromQueue(request.playerName)
                            val battleId =
                              if (ticket % 2 == 0) opponentTicket + "-" + ticket
                              else ticket + "-" + opponentTicket
                            AsyncDbManager.createBattleInstance(request.playerName, opponentName, battleId).onComplete {
                              case Success(_) => {
                                sendBattleDataToBoth((request.playerName, request.team, replyTo.get),
                                                     (opponentName, opponentTeam, opponentReplyTo),
                                                     battleId)
                              }
                              case Failure(e) => println(s"Failure... Caught: $e")
                            }
                          }
                          case Failure(e) => println(s"Failure... Caught: $e")
                          case _          => println(s"Could not find opponent with ticket $opponentTicket")
                        }
                      }
                      case Failure(e) => println(s"Failure... Caught: $e")
                    }
                  case Failure(e) => println(s"Failure... Caught: $e")
                }
              case "Remove" =>
                AsyncDbManager.removePlayerFromQueue(request.playerName).onComplete {
                  case Success(_)         => println("Client removed successfully")
                  case Failure(exception) => println(s"Failure... Caught: $exception")
                }
            }
          }
          ack
        }
      }
    }
  }

  def sendBattleDataToBoth(dataReqPlayer: (String, Seq[String], String),
                           dataQueuedPlayer: (String, Seq[String], String),
                           battleId: String): Unit = {
    val responseForRequester = JoinCasualQueueResponse(
      Right((dataQueuedPlayer._1, dataQueuedPlayer._2.toList, battleId)))
    val responseForQueued = JoinCasualQueueResponse(Right((dataReqPlayer._1, dataReqPlayer._2.toList, battleId)))
    rabbitControl ! Message.queue(responseForRequester, dataReqPlayer._3)
    rabbitControl ! Message.queue(responseForQueued, dataQueuedPlayer._3)
  }

}
