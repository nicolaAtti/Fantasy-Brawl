package loginguest

import scala.concurrent.Future

import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.bson.conversions.Bson

/** Provides functionality to manage the asynchronous interaction between the
  * login service and a MongoDb.
  */
object MongoDbManager extends AsyncDbManager {

  import config.DbNaming._
  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val collection = database.getCollection(Login.CollectionName)

  override def nextGuestNumber: Future[Int] = {
    val incByOne: Bson = inc(Login.GuestNumber, number = 1)

    collection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = Login.GuestsDocumentId), update = incByOne)
      .map(oldDocument => oldDocument(Login.GuestNumber).asInt32().getValue)
      .head
  }
}
