import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DbManager extends App {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")

  val collection = database.getCollection("counters")
  val guestsDocumentId = "guests"
  val numberField = "number"

  def nextGuestNumber: Future[Int] = {
    val filter = Filters.equal(fieldName = "_id", value = "guests")
    val incrementByOne = inc(fieldName = "number", number = 1)
    collection
      .findOneAndUpdate(filter, incrementByOne)
      .toFuture()
      .map(oldDocument => oldDocument(numberField).asInt32().getValue)
  }
}
