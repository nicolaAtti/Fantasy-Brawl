package matchmaking

import org.mongodb.scala.bson.BsonValue
import com.mongodb.client.model.Updates
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, Document, MongoClient}

import scala.concurrent.Future

object MongoDbManager {

  import config.DbNaming._

  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val casualQueueCollection = database.getCollection(CasualQueue.CollectionName)
  val battleCollection = database.getCollection(Battles.CollectionName)

  /** Retrieves a ticket from the casual queue and increments the tickets counter.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the casual queue ticket
    */
  def getTicket: Future[Int] = {
    casualQueueCollection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = CasualQueue.TicketsDocumentId),
                        update = inc(CasualQueue.TicketNumber, number = 1))
      .map(oldDocument => oldDocument(CasualQueue.TicketNumber).asInt32.getValue)
      .head
  }

  /** Creates a new document representing a new player inside the casual queue.
    *
    * @param playerName  the player that joins the queue
    * @param teamMembers the player's tea,
    * @param replyTo     the player response queue
    * @return a Future representing the success/failure of the operation
    */
  def putPlayerInQueue(ticket: Int,
                       playerName: String,
                       teamMembers: Set[String],
                       battleQueue: String,
                       replyTo: String): Future[Completed] = {
    casualQueueCollection
      .insertOne(
        Document(
          "_id" -> ticket,
          CasualQueue.PlayerName -> playerName,
          CasualQueue.TeamMembers -> teamMembers.toSeq,
          CasualQueue.BattleQueue -> battleQueue,
          CasualQueue.ReplyTo -> replyTo,
          CasualQueue.LeftTheQueue -> false
        ))
      .head
  }

  /** Searches for a document with the information of a queued player with a
    * certain ticket and removes it.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the player's name, team and response queue
    */
  def takePlayerFromQueue(ticket: Int): Future[(String, Set[String], String, String, Boolean)] = {
    casualQueueCollection
      .findOneAndDelete(filter = Filters.equal(fieldName = "_id", value = ticket))
      .map(
        document =>
          (document(CasualQueue.PlayerName).asString.getValue,
           document(CasualQueue.TeamMembers)
             .asArray()
             .toArray
             .map { case bsonTeamMember: BsonValue => bsonTeamMember.asString.getValue }
             .toSet,
           document(CasualQueue.BattleQueue).asString.getValue,
           document(CasualQueue.ReplyTo).asString.getValue,
           document(CasualQueue.LeftTheQueue).asBoolean.getValue))
      .head
  }

  /** Removes an existing document of a queued player from the DB.
    *
    * @param playerName the player to remove
    * @return a Future representing the success/failure of the deletion
    */
  def removePlayerFromQueue(playerName: String): Future[DeleteResult] = {
    casualQueueCollection
      .deleteOne(filter = Filters.equal(CasualQueue.PlayerName, playerName))
      .head
  }

  /** Notifies that a player has left the game while waiting for an opponent.
    *
    * @param playerName the name of the player that left the game
    * @return a Future representing the success/failure of the update
    */
  def notifyPlayerLeft(playerName: String): Future[UpdateResult] = {
    casualQueueCollection
      .updateOne(filter = Filters.equal(CasualQueue.PlayerName, playerName),
                 update = Updates.set(CasualQueue.LeftTheQueue, true))
      .head
  }

  /** Creates a new battle instance inside the battles collection.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation
    */
  def createBattleInstance(battleId: String): Future[Completed] = {
    battleCollection
      .insertOne(Document("_id" -> battleId, Battles.CurrentRound -> 0))
      .head
  }

}
