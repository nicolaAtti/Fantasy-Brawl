package matchmaking

import org.mongodb.scala.bson.BsonValue
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, Document, MongoClient}

import scala.concurrent.Future

object MongoDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val queueCollection = database.getCollection("casual-queue")
  val battleCollection = database.getCollection("active-battles")

  val IdFieldName = "_id"
  val PlayerFieldName = "playerName"
  val TeamMembersFieldName = "teamMembers"
  val ReplyToFieldName = "replyTo"
  val FirstPlayerFieldName = "firstPlayerName"
  val SecondPlayerFieldName = "secondPlayerName"

  val ticketDocumentId = "casual-queue-ticket"
  val field = "ticket"

  def getTicket: Future[Int] = {
    queueCollection
      .findOneAndUpdate(filter = Filters.equal(IdFieldName, value = ticketDocumentId), update = inc(field, number = 1))
      .map(oldDocument => oldDocument(field).asInt32().getValue)
      .head()
  }

  /**
    * Search for a document in the DB representing a queued player and remove it
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the player's name,team and response queue
    */
  def takePlayerFromQueue(ticket: Int): Future[(String, Seq[String], String)] = {
    queueCollection
      .findOneAndDelete(filter = Filters.equal(IdFieldName, value = ticket))
      .map(document =>
        (document(PlayerFieldName).asString().getValue, document(TeamMembersFieldName).asArray().toArray.toSeq.collect {
          case str: BsonValue => str.asString().getValue
        }, document(ReplyToFieldName).asString().getValue))
      .head()
  }

  /**
    * Creates a new document inside the DB representing a new player inside the casual queue
    *
    * @param playerName the player that joins the queue
    * @param teamMembers the player's tea,
    * @param replyTo the player response queue
    * @return a Future representing the success/failure of the operation
    */
  def putPlayerInQueue(ticket: Int,
                       playerName: String,
                       teamMembers: Seq[String],
                       replyTo: String): Future[Completed] = {
    queueCollection
      .insertOne(
        Document(IdFieldName -> ticket,
                 PlayerFieldName -> playerName,
                 TeamMembersFieldName -> teamMembers,
                 ReplyToFieldName -> replyTo))
      .head()
  }

  /**
    * Creates a new document inside the DB representing a battle instance
    *
    * @param player1Name the first player
    * @param player2Name the second player
    * @return a Future representing the success/failure of the operation
    */
  def createBattleInstance(player1Name: String, player2Name: String, battleId: String): Future[Completed] = {
    battleCollection
      .insertOne(
        Document(IdFieldName -> battleId, FirstPlayerFieldName -> player1Name, SecondPlayerFieldName -> player2Name))
      .head()
  }

  /**
    * Removes an existing document of a queued player from the DB
    *
    * @param playerName the player to remove
    * @return a Future representing the success/failure of the operation
    */
  def removePlayerFromQueue(playerName: String): Future[DeleteResult] = {
    queueCollection.deleteOne(Filters.equal(PlayerFieldName, playerName)).head()
  }

}
