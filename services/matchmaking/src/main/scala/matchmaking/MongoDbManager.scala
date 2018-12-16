package matchmaking

import org.mongodb.scala.bson.BsonValue
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, Document, MongoClient}

import scala.concurrent.Future

object MongoDbManager {

  import config.DbNaming._
  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val casualQueueCollection = database.getCollection(CasualQueue.CollectionName)
  val battleCollection = database.getCollection(ActiveBattles.CollectionName)

  /** Retrieves a ticket from the casual queue and increments the tickets counter.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the casual queue ticket
    */
  def getTicket: Future[Int] = {
    casualQueueCollection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = CasualQueue.TicketsDocumentId),
                        update = inc(CasualQueue.Ticket, number = 1))
      .map(oldDocument => oldDocument(CasualQueue.Ticket).asInt32().getValue)
      .head()
  }

  /** Creates a new document representing a new player inside the casual queue
    *
    * @param playerName the player that joins the queue
    * @param teamMembers the player's tea,
    * @param replyTo the player response queue
    * @return a Future representing the success/failure of the operation
    */
  def putPlayerInQueue(ticket: Int,
                       playerName: String,
                       teamMembers: Set[String],
                       replyTo: String): Future[Completed] = {
    casualQueueCollection
      .insertOne(
        Document("_id" -> ticket,
                 CasualQueue.PlayerName -> playerName,
                 CasualQueue.TeamMembers -> teamMembers.toSeq,
                 CasualQueue.ReplyTo -> replyTo))
      .head()
  }

  /** Searches for a document with the information of a queued player with a
    * certain ticket and removes it.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the player's name, team and response queue
    */
  def takePlayerFromQueue(ticket: Int): Future[(String, Set[String], String)] = {
    casualQueueCollection
      .findOneAndDelete(filter = Filters.equal(fieldName = "_id", value = ticket))
      .map(
        document =>
          (document(CasualQueue.PlayerName).asString().getValue,
           document(CasualQueue.TeamMembers)
             .asArray()
             .toArray
             .toSeq
             .collect {
               case str: BsonValue => str.asString().getValue
             }
             .toSet,
           document(CasualQueue.ReplyTo).asString().getValue))
      .head()
  }

  /** Removes an existing document of a queued player from the DB
    *
    * @param playerName the player to remove
    * @return a Future representing the success/failure of the operation
    */
  def removePlayerFromQueue(playerName: String): Future[DeleteResult] = {
    casualQueueCollection.deleteOne(Filters.equal(CasualQueue.PlayerName, playerName)).head()
  }

  /** Creates a new battle instance inside the battles collection.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation
    */
  def createBattleInstance(battleId: String): Future[Completed] = {
    battleCollection
      .insertOne(Document("_id" -> battleId, ActiveBattles.CurrentRound -> 0))
      .head()
  }

}
