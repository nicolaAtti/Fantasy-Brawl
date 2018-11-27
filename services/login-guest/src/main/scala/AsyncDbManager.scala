import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._

/** Useful constants and a function to manage the asynch communication with a mongo database
  * @author Marco Canducci
  */
object AsyncDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val collection = database.getCollection("counters")
  val documentId = "guests"
  val field = "number"

  val filter = Filters.equal("_id", value = documentId)
  val incrementByOne = inc(field, number = 1)

  def nextGuestNumber: Future[Int] = {
    collection
      .findOneAndUpdate(filter, incrementByOne)
      .map(oldDocument => oldDocument(field).asInt32().getValue)
      .head() // Returns the head of the Observable in a Future
  }

}
