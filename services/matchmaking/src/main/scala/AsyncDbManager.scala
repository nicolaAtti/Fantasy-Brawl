import scala.concurrent.Future
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult

object AsyncDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val queueCollection = database.getCollection("casual-queue")
  val battleCollection = database.getCollection("active-battles")
  val documentId = "casual-queue"

  val filter = Filters.equal("_id", value = documentId)

  /**
    * Search for a document in the DB representing a queued player and remove it
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the player's name,team and response queue
    */
  def findPlayerInQueue: Future[(String, Seq[String], String)] = {
    queueCollection
      .findOneAndDelete(filter)
      .map(document =>
        (document("playerName").asString().getValue, document("teamMembers").asArray().toArray.toSeq.collect {
          case str: BsonValue => str.asString().getValue
        }, document("replyToQueue").asString().getValue))
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
  def putPlayerInQueue(playerName: String, teamMembers: Seq[String], replyTo: String): Future[Completed] = {
    queueCollection
      .insertOne(
        Document("_id" -> documentId,
                 "playerName" -> playerName,
                 "teamMembers" -> teamMembers,
                 "replyToQueue" -> replyTo))
      .head()
  }

  /**
    * Creates a new document inside the DB representing a battle instance
    *
    * @param player1Name the first player
    * @param player2Name the second player
    * @return a Future representing the success/failure of the operation
    */
  def createBattleInstance(player1Name: String, player2Name: String): Future[Completed] = {
    battleCollection
      .insertOne(Document("player1" -> player1Name, "player2" -> player2Name))
      .head()
  }

  /**
    * Removes an existing document of a queued player from the DB
    *
    * @param playerName the player to remove
    * @return a Future representing the success/failure of the operation
    */
  def removeQueuedPlayer(playerName: String): Future[DeleteResult] = {
    queueCollection.deleteOne(Filters.equal("playerName", playerName)).head()
  }

}
