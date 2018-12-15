import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import config.MessagingSettings

/** Entry point of the service that handles the login for guests users.
  * @author Marco Canducci
  */
object Main extends App {

  import communication._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  implicit val requestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit val responseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  import Queues._

  var boh: Option[(Map[(String, String), Int], Int, String)] = None

  val requestQueue =
    Queue(StartRoundRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[StartRoundRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (boh.isEmpty) {
              boh = Some(request.myTeamSpeeds.map {
                case (characterName, speed) => (request.playerName, characterName) -> speed
              }, request.round, replyTo.get)
            } else {
              println(request.myTeamSpeeds.map {
                case (characterName, speed) => (request.playerName, characterName) -> speed
              })
              println(boh.get._1)
              val speedsNotOrdered = (request.myTeamSpeeds.map {
                case (characterName, speed) => (request.playerName, characterName) -> speed
              } ++ boh.get._1).toList
              val speedsOrdered: List[(String, String)] =
                speedsNotOrdered.sortBy(_._2).reverse.map(c => c._1._1 -> c._1._2)
              println(speedsOrdered)
              val response = StartRoundResponse(Right(speedsOrdered), request.round)
              rabbitControl ! Message.queue(response, replyTo.get)
              rabbitControl ! Message.queue(response, boh.get._3)
              boh = None
            }
            ack
          }
        }
      }
    }
  }
}
