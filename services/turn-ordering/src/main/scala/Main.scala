import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo

/** Entry point of the service that handles the login for guests users.
  * @author Marco Canducci
  */
object Main extends App {

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(requeue = false)

  import communication._

  implicit val requestFormat = MessageFormat.format[StartRoundRequest]
  implicit val responseFormat = MessageFormat.format[StartRoundResponse]

  import Queues._

  var boh: Option[(Map[(String, String), Int], Int, String)] = None

  val requestQueue = Queue(StartRoundRequestQueue, durable = false, autoDelete = true)
  Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = 3) {
      consume(requestQueue) {
        (body(as[StartRoundRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (boh.isEmpty) {
              boh = Some(request.myTeamSpeeds.map {
                case (characterName, speed) => (request.playerName, characterName) -> speed
              }, request.round, replyTo.get)
            } else {
              val speedsNotOrdered = (request.myTeamSpeeds.map {
                case (characterName, speed) => (request.playerName, characterName) -> speed
              } ++ boh.get._1).toList
              val speedsOrdered: List[(String, String)] = speedsNotOrdered.sortBy(_._2).map(c => c._1._1 -> c._1._2)
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
