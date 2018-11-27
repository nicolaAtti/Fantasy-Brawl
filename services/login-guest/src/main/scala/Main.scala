import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import play.api.libs.json._
import PlayJsonSupport._

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global

/** Entry point of the service that handles the login for guests users.
  *
  * It reads and updates (incrementing by one) a counter that acts as unique guest identifier.
  * @author Marco Canducci
  */
object Main extends App {

  final val Log = true
  final val LogMessage = "Received a new login request"
  final val LogDetailsPrefix = "Details: "

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(requeue = false)

  import messages._
  implicit val requestFormat = Json.format[LoginGuestRequest]
  implicit val responseFormat = Json.format[LoginGuestResponse]

  import Queues._
  val requestQueue = Queue(LoginGuestRequestQueue, durable = false, autoDelete = true)

  val subscription = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(requestQueue) {
        (body(as[LoginGuestRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            if (Log) {
              println(LogMessage)
              request.details match {
                case Some(d) => println(LogDetailsPrefix + d)
                case _       => Unit
              }
            }

            AsyncDbManager.nextGuestNumber.onComplete {
              case Success(optN: Option[Int]) =>
                optN match {
                  case Some(n) => sendGuestNumber(n, clientQueue = replyTo.get)
                  case None    => println("Cannot find the next guest number")
                }
              case Failure(exc) => println(s"Failure... Caught: $exc")
            }
          }
          ack
        }
      }
    }
  }

  def sendGuestNumber(number: Int, clientQueue: String): Unit = {
    val response = LoginGuestResponse(guestId = Some(number), details = None)
    rabbitControl ! Message.queue(response, clientQueue)
  }
}
