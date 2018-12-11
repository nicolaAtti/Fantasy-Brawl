import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat

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
                AsyncDbManager.findPlayerInQueue.onComplete {
                  case Success(opponentData: (String, Seq[String], String)) =>
                    val reqPlayerData = (request.playerName, request.team, replyTo.get)
                    AsyncDbManager.createBattleInstance(request.playerName, opponentData._1).onComplete {
                      case Success(_) => sendBattleDataToBoth(reqPlayerData, opponentData)
                      case Failure(e) => println(s"Failure... Caught: $e")
                    }

                  case Failure(e) =>
                    println(s"Failure... Caught: $e")
                  case _ =>
                    AsyncDbManager
                      .putPlayerInQueue(request.playerName.toString, request.team, replyTo.get.toString)
                      .onComplete {
                        case Success(_)         => println("Database updated successfully")
                        case Failure(exception) => println(s"Failure... Caught: $exception")
                      }
                }
              case "Remove" =>
                AsyncDbManager.removeQueuedPlayer(request.playerName).onComplete {
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
                           dataQueuedPlayer: (String, Seq[String], String)): Unit = {
    val responseForRequester = JoinCasualQueueResponse(Right((dataQueuedPlayer._1, dataQueuedPlayer._2)))
    val responseForQueued = JoinCasualQueueResponse(Right((dataReqPlayer._1, dataReqPlayer._2)))
    rabbitControl ! Message.queue(responseForRequester, dataReqPlayer._3)
    rabbitControl ! Message.queue(responseForQueued, dataQueuedPlayer._3)
  }

}
