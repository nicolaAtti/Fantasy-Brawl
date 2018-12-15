import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import config.MessagingSettings

object Main extends App {

  import communication._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  implicit val requestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit val responseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  import Queues._

  val requestQueue =
    Queue(StartRoundRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[StartRoundRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {

            ack
          }
        }
      }
    }
  }
}
