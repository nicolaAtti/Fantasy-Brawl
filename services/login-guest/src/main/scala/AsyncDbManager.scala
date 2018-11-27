import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import org.mongodb.scala._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._

object AsyncDbManager extends App {

  def nextGuestNumber: Future[Option[Int]] = {

    val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
    val database = mongoClient.getDatabase("heroku_3bppsqjk")

    val collection = database.getCollection("counters")
    val documentId = "guests"
    val field = "number"

    val filter = Filters.equal("_id", value = documentId)
    val incrementByOne = inc(field, number = 1)

    collection
      .findOneAndUpdate(filter, incrementByOne)
      .map(oldDocument => oldDocument("number").asInt32().getValue)
      .headOption()
  }
}
