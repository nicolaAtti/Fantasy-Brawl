package loginguest

import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc

import scala.concurrent.Future

/** Provides a function and a set of constants to manage the asynchronous
  * communication with a nosql database.
  *
  * @author Marco Canducci
  */
object MongoDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val collection = database.getCollection("counters")
  val documentId = "guests"
  val field = "number"

  val filter = Filters.equal("_id", value = documentId)
  val incrementByOne = inc(field, number = 1)

  /** Atomically returns and updates (increasing it by one) a counter inside
    * a remote nosql database.
    *
    * @return a Future containing the unique guest number
    */
  def nextGuestNumber: Future[Int] = {
    collection
      .findOneAndUpdate(filter, incrementByOne)
      .map(oldDocument => oldDocument(field).asInt32().getValue)
      .head() // returns the Observable's head in a Future
  }

}