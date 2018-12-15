import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.Config
import communication.MessageFormat.MyFormat
import loginguest.MongoDbManager

/** Entry point of the service that handles the login for guests users.
  * @author Marco Canducci
  */
object Main extends App {

  final val Log = true
  final val LogMessage = "Received a new login request"
  final val LogDetailsPrefix = "Details: "
  final val FailurePrint = "Failure... Caught:"

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = Config.Requeue)

  import communication._
  implicit val requestFormat: MyFormat[LoginGuestRequest] = MessageFormat.format[LoginGuestRequest]
  implicit val responseFormat: MyFormat[LoginGuestResponse] = MessageFormat.format[LoginGuestResponse]

  import Queues._
  val requestQueue = Queue(LoginGuestRequestQueue, durable = Config.Durable, autoDelete = Config.AutoDelete)

  val subscription = Subscription.run(rabbitControl) {
    import Directives._
    channel(qos = Config.Qos) {
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

            MongoDbManager.nextGuestNumber.onComplete {
              case Success(n: Int) => sendGuestNumber(n, clientQueue = replyTo.get)
              case Failure(e)      => println(s"$FailurePrint $e")
            }
          }
          ack
        }
      }
    }
  }

  def sendGuestNumber(number: Int, clientQueue: String): Unit = {
    val response = LoginGuestResponse(Right(number))
    rabbitControl ! Message.queue(response, clientQueue)
  }
}
