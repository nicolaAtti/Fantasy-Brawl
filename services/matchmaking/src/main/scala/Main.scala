import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit.properties.ReplyTo
import com.spingo.op_rabbit.{Queue, RabbitControl, RecoveryStrategy, Subscription}
import org.mongodb.scala.bson.collection.mutable.Document
import play.api.libs.json.Json

import scala.util.{Failure, Success}

object Main extends App {

  type PlayerData = (String,Seq[String],String)

  final val Log = true
  final val LogMessage = "Received a new casual queue join request"
  final val LogDetailsPrefix = "Details: "

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(requeue = false)

  import communication._
  implicit val requestFormat = Json.format[JoinCasualQueueRequest]
  implicit val responseFormat = Json.format[JoinCasualQueueResponse]

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
          }
          AsyncDbManager.findPlayerInQueue.onComplete{
            case Success(document: Document) => val reqPlayerData = (request.playerName,request.team,request.replyto)
                                                val opponentData = ()

                                                sendBattleDataToBoth(reqPlayerData, opponentData)



            case Failure(e) => println(s"Failure... Caught: $e")
                                AsyncDbManager.putPlayerInQueue(request.playerName,request.team,request.replyto)
          }
        }
          ack
        }
      }
    }

  /*def sendGuestNumber(number: Int, clientQueue: String): Unit = {
    val response = LoginGuestResponse(guestId = Some(number), details = None)
    rabbitControl ! Message.queue(response, clientQueue)
  }*/

  def sendBattleDataToBoth(data: PlayerData, data1: PlayerData): Unit = ???

}