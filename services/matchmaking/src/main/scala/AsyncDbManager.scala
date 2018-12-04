import scala.concurrent.Future
import org.mongodb.scala._
import org.mongodb.scala.model.Filters

object AsyncDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val collection = database.getCollection("casual-queue")
  val documentId = "casual-queue"

  val filter = Filters.equal("_id", value = documentId)

  def findPlayerInQueue: Future[(String, Seq[String], String)] = {
    val arr = Array[String]()
    collection
      .findOneAndDelete(filter)
      .map(
        document =>
          (document("playerName").asString().toString,
           document("teamMembers").asArray().toArray[String](arr).toSeq,
           document("replyToQueue").asString().toString))
      .head()
  }

  def putPlayerInQueue(playerName: String, teamMembers: Seq[String], replyTo: String): SingleObservable[Completed] = {
    println("poroo")
    collection.insertOne(
      Document("_id" -> documentId,
               "playerName" -> playerName,
               "teamMembers" -> teamMembers,
               "replytoQueue" -> replyTo))
  }

}
