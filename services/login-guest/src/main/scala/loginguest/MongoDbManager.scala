package loginguest

import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.bson.conversions.Bson

import scala.concurrent.Future

/** Provides a function and a set of constants to manage the asynchronous
  * communication with a Mongo database.
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
      .head
  }
}
