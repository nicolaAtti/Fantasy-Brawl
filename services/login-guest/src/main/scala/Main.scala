import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit.properties.ReplyTo
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.Directives.ack
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import play.api.libs.json._
import PlayJsonSupport._
import org.mongodb.scala.Completed

import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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

//  import ExecutionContext.Implicits.global


  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")

  val collection = database.getCollection("counters")
  val guestsDocumentId = "guests"
  val numberField = "number"


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

//            nextGuestNumber.onComplete {
//              case Success(n: Int)       => sendGuestNumber(n, clientQueue = replyTo.get)
//              case Failure(e: Throwable) => println("Failed") // tech debt: handle the Throwable somehow
//            }

            val filter = Filters.equal(fieldName = "_id", value = "guests")
            val incrementByOne = inc(fieldName = "number", number = 1)
            collection
              .findOneAndUpdate(filter, incrementByOne)
              .subscribe((oldDocument: Document) => {
                val number = oldDocument("number").asInt32().getValue
                println(number)
                sendGuestNumber(number, clientQueue = replyTo.get)
              })

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

  def nextGuestNumber: Future[Int] = {
    val filter = Filters.equal(fieldName = "_id", value = "guests")
    val incrementByOne = inc(fieldName = "number", number = 1)
    collection
      .findOneAndUpdate(filter, incrementByOne)
      .toFuture()
      .map(oldDocument => oldDocument(numberField).asInt32().getValue)
  }

}
