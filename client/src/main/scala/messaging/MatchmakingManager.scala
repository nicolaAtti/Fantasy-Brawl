package messaging

import akka.actor.{ActorRef, ActorSystem, Props}
import com.spingo.op_rabbit.PlayJsonSupport._
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication._
import play.api.libs.json.{Json, OFormat}
import view.ViewConfiguration.viewSelector._
import view._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Manages login as a guest request and response messages.
  *
  * @author Daniele Schiavi
  */
object MatchmakingManager {
  private val rabbitControl: ActorRef = ActorSystem().actorOf(Props[RabbitControl])
  implicit private val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(false)

  import Queues._
  private val joinCasualMatchmakingRequestQueue =
    Queue(JoinCasualMatchmakingRequestQueue, durable = false, autoDelete = true)
  private val joinCasualMatchmakingResponseQueue =
    Queue(JoinCasualMatchmakingResponseQueue, durable = false, autoDelete = true)

  implicit private val RequestFormat: OFormat[JoinCasualQueueRequest] = Json.format[JoinCasualQueueRequest]
  implicit private val ResponseFormat: OFormat[JoinCasualQueueResponse] = Json.format[JoinCasualQueueResponse]

  private val publisher: Publisher = Publisher.queue(joinCasualMatchmakingRequestQueue)

  /**
    * Manages login as a guest response messages.
    */
  Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(joinCasualMatchmakingResponseQueue) {
        body(as[JoinCasualQueueRequest]) { response =>
          ack
        }
      }
    }
  }

  /**
    * Send a login as a guest request message.
    */
  def joinCasualQueueRequest(playerName: String, team: Map[String, String]): Unit = {
    import PlayJsonSupport._
    rabbitControl ! Message(JoinCasualQueueRequest(playerName, team),
                            publisher,
                            Seq(ReplyTo(joinCasualMatchmakingResponseQueue.queueName)))
  }
}
