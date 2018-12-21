import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import config.MessagingSettings
import loginguest.{AsyncDbManager, MongoDbManager}
import communication._
import Queues._
import config.MiscSettings._

/** Entry point of the service that handles the login for guests users.
  *
  * @author Marco Canducci
  */
object Main extends App {

  val LogMessage = "Received a new login request"
  val dbManager: AsyncDbManager = MongoDbManager

  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)
  implicit val requestFormat: MyFormat[LoginGuestRequest] = MessageFormat.format[LoginGuestRequest]
  implicit val responseFormat: MyFormat[LoginGuestResponse] = MessageFormat.format[LoginGuestResponse]

  val requestQueue =
    Queue(LoginGuestRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])

  val subscription = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[LoginGuestRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {

            if (ServicesLog) {
              println(LogMessage)
              request.details match {
                case Some(d) => println(LogDetailsPrefix + d)
                case _       => Unit
              }
            }

            dbManager.nextGuestNumber.onComplete {
              case Success(n: Int) => sendGuestNumber(n, clientQueue = replyTo.get)
              case Failure(e)      => println(s"$LogFailurePrefix $e")
            }
          }
          ack
        }
      }
    }
  }

  /** Sends an unique number to the client that wanted to login as a guest.
    *
    * @param number the unique guest number
    * @param clientQueue the client's response queue
    */
  private def sendGuestNumber(number: Int, clientQueue: String): Unit = {
    val response = LoginGuestResponse(Right(number))
    rabbitControl ! Message.queue(response, clientQueue)
  }
}
