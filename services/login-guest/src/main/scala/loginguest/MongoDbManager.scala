package loginguest

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc

import scala.concurrent.Future

/** Provides a function and a set of constants to manage the asynchronous
  * communication with a nosql database.
  *
  * @author Marco Canducci
  */
object MongoDbManager {

  import config.DbNaming._
  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val collection = database.getCollection(Login.CollectionName)

  /** Atomically returns and updates (increasing it by one) the guests counter.
    *
    * @return a Future with the unique guest number
    */
  def nextGuestNumber: Future[Int] = {
    val incByOne: Bson = inc(Login.GuestNumber, number = 1)

    collection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = Login.GuestsDocumentId), update = incByOne)
      .map(oldDocument => oldDocument(Login.GuestNumber).asInt32().getValue)
      .head() // returns the Observable's head in a Future
  }

}
