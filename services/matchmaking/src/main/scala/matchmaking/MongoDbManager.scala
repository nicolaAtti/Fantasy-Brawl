package matchmaking

import org.mongodb.scala.bson.BsonValue
import com.mongodb.client.model.Updates
import communication.matchmaking.PlayerInfo
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.{Document, MongoClient}
import scala.concurrent.Future

/** Provides functionality to manage the asynchronous interaction between the
  * matchmaking service and a MongoDB database.
  *
  * @author Nicola Atti, Marco Canducci
  */
object MongoDbManager extends AsyncDbManager {

  import config.DbNaming._

  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val casualQueueCollection = database.getCollection(CasualQueue.CollectionName)
  val battleCollection = database.getCollection(Battles.CollectionName)

  override def getTicket: Future[Int] = {
    casualQueueCollection
      .findOneAndUpdate(filter = Filters.equal(fieldName = "_id", value = CasualQueue.TicketsDocumentId),
                        update = inc(CasualQueue.TicketNumber, number = 1))
      .map(oldDocument => oldDocument(CasualQueue.TicketNumber).asInt32.getValue)
      .head
  }

  override def putPlayerInQueue(ticket: Int, playerInfo: PlayerInfo, replyTo: String): Future[String] = {
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
      .map(_ => s"Player ${playerInfo.name} successfully inserted in queue.")
      .head
  }

  override def takePlayerFromQueue(ticket: Int): Future[Option[(PlayerInfo, String, Boolean)]] = {
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
      .headOption()
  }

  override def removePlayerFromQueue(playerName: String): Future[Boolean] = {
    casualQueueCollection
      .deleteOne(filter = Filters.equal(CasualQueue.PlayerName, playerName))
      .map(deleteResult => deleteResult.getDeletedCount > 0)
      .head
  }

  override def notifyPlayerLeft(playerName: String): Future[Boolean] = {
    casualQueueCollection
      .updateOne(filter = Filters.equal(CasualQueue.PlayerName, playerName),
                 update = Updates.set(CasualQueue.LeftTheQueue, true))
      .map(updateResult => updateResult.getModifiedCount > 0)
      .head
  }

  override def createBattleInstance(battleId: String): Future[String] = {
    battleCollection
      .insertOne(Document("_id" -> battleId, Battles.CurrentRound -> 0))
      .map(_ => s"Battle $battleId created.")
      .head
  }

}
