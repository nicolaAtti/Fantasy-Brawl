import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global

import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo

object Main extends App {

  type PlayerData = (String, Seq[String], String)

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
            AsyncDbManager.findPlayerInQueue.onComplete {
             case Success(opponentData: PlayerData) =>
               println("critssss")
                val reqPlayerData = (request.playerName, request.team, replyTo.get)
                sendBattleDataToBoth(reqPlayerData, opponentData)
              case Failure(e) =>
                println(s"Failure... Caught: $e")
             case _ =>
                AsyncDbManager.putPlayerInQueue(request.playerName, request.team, replyTo.get)
            }
          }
          ack
        }
      }
    }
  }

  def sendBattleDataToBoth(data: PlayerData, data1: PlayerData): Unit = {
    println("Player 1 : " + data._1 + "  " + data._2.toString() + "   " + data._3)
    println("Player 2 : " + data1._1 + "  " + data1._2.toString() + "   " + data1._3)
  }

}
