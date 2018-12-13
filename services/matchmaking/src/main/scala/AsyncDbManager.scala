import scala.concurrent.Future
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates._

object AsyncDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val queueCollection = database.getCollection("casual-queue")
  val battleCollection = database.getCollection("active-battles")

  val ticketDocumentId = "casual-queue-ticket"
  val field = "ticket"

  def getTicket: Future[Int] = {
    queueCollection
      .findOneAndUpdate(filter = Filters.equal("_id", value = ticketDocumentId), update = inc(field, number = 1))
      .map(oldDocument => oldDocument(field).asInt32().getValue)
      .head()
  }

  def takePlayerFromQueue(ticket: Int): Future[(String, Seq[String], String)] = {
    queueCollection
      .findOneAndDelete(filter = Filters.equal("_id", value = ticket))
      .map(document =>
        (document("playerName").asString().getValue, document("teamMembers").asArray().toArray.toSeq.collect {
          case str: BsonValue => str.asString().getValue
        }, document("replyTo").asString().getValue))
      .head()
  }

  def putPlayerInQueue(ticket: Int,
                       playerName: String,
                       teamMembers: Seq[String],
                       replyTo: String): Future[Completed] = {
    queueCollection
      .insertOne(
        Document("_id" -> ticket, "playerName" -> playerName, "teamMembers" -> teamMembers, "replyTo" -> replyTo))
      .head()
  }

  def createBattleInstance(player1Name: String, player2Name: String, battleId: String): Future[Completed] = {
    battleCollection
      .insertOne(Document("_id" -> battleId, "player1" -> player1Name, "player2" -> player2Name))
      .head()
  }

  def removePlayerFromQueue(playerName: String): Future[DeleteResult] = {
    queueCollection.deleteOne(Filters.equal("playerName", playerName)).head()
  }

}
