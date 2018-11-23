import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit.properties.ReplyTo
import com.spingo.op_rabbit._

import scala.concurrent.ExecutionContext
import play.api.libs.json._
import PlayJsonSupport._

/** Entry point of the service that handles the login for guests users.
  *
  * It reads and update (incrementing by one) a counter that acts as unique guest identifier.
  */
object Main extends App {

  final val Log = true
  final val LogMessage = "Received a new login request"
  final val LogDetailsPrefix = "Details: "

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy = RecoveryStrategy.nack(false)

  import messages._

  implicit val requestFormat = Json.format[LoginGuestRequest]
  implicit val responseFormat = Json.format[LoginGuestResponse]

  import Queues._
  val requestQueue = Queue(LoginGuestRequestQueue, durable = false, autoDelete = true)

  import ExecutionContext.Implicits.global

  val subscription = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = 3) {
      consume(requestQueue) {
        (body(as[LoginGuestRequest]) & optionalProperty(ReplyTo)) {

          (request, replyTo) => {
            if (Log) {
              println(LogMessage)
              request.details match {
                case Some(d) => println(LogDetailsPrefix + d)
                case _       => Unit
              }
            }

            val response = LoginGuestResponse(guestId = Some(getAndUpdateFromDb), details = None)
            rabbitControl ! Message.queue(response, queue = replyTo.get)
            ack
          }

        }
      }
    }
  }

  // Temporary mockup function
  def getAndUpdateFromDb: Int = {
    scala.util.Random.nextInt()
  }

}
