package matchmaking

import org.mongodb.scala.bson.BsonValue
import com.mongodb.client.model.Updates
import communication.matchmaking.PlayerInfo
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, Document, MongoClient}

import scala.concurrent.Future

/** Provides functionality for the database asynchronous interactions.
  *
  * @author Nicola Atti, Marco Canducci
  */
object MongoDbManager {

  import config.DbNaming._

  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val casualQueueCollection = database.getCollection(CasualQueue.CollectionName)
  val battleCollection = database.getCollection(Battles.CollectionName)

  /** Retrieves a ticket from the casual queue and increments the ticket counter.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the casual queue's ticket number
    */
  def getTicket: Future[Int] = {
    casualQueueCollection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = CasualQueue.TicketsDocumentId),
                        update = inc(CasualQueue.TicketNumber, number = 1))
      .map(oldDocument => oldDocument(CasualQueue.TicketNumber).asInt32.getValue)
      .head
  }

  /** Creates a document representing a new player inside the casual queue.
    *
    * @param ticket the player's ticket for the matchmaking queue
    * @param playerInfo the player's information
    * @param replyTo the player response queue
    * @return a Future representing the success/failure of the operation
    */
  def putPlayerInQueue(ticket: Int, playerInfo: PlayerInfo, replyTo: String): Future[Completed] = {
    casualQueueCollection
      .insertOne(
        Document(
          "_id" -> ticket,
          CasualQueue.PlayerName -> playerInfo.name,
          CasualQueue.TeamMembers -> playerInfo.teamNames.toSeq,
          CasualQueue.BattleQueue -> playerInfo.battleQueue,
          CasualQueue.ReplyTo -> replyTo,
          CasualQueue.LeftTheQueue -> false
        ))
      .head
  }

  /** Searches for the player information associated with the given ticket and,
    * if present, removes them.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the player's information, the response queue and a flag
    *         that represents if the player has left the queue or not.
    */
  def takePlayerFromQueue(ticket: Int): Future[(PlayerInfo, String, Boolean)] = {
    casualQueueCollection
      .findOneAndDelete(filter = Filters.equal(fieldName = "_id", value = ticket))
      .map(document =>
        (PlayerInfo(
           name = document(CasualQueue.PlayerName).asString.getValue,
           teamNames = document(CasualQueue.TeamMembers)
             .asArray()
             .toArray
             .map { case bsonTeamMember: BsonValue => bsonTeamMember.asString.getValue }
             .toSet,
           battleQueue = document(CasualQueue.BattleQueue).asString.getValue
         ),
         document(CasualQueue.ReplyTo).asString.getValue,
         document(CasualQueue.LeftTheQueue).asBoolean.getValue))
      .head
  }

  /** Removes the existing document of a queued player given its name.
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
    * @param playerName the name of the player that left the queue
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
