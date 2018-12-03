import scala.concurrent.Future
import org.mongodb.scala._
import org.mongodb.scala.model.Filters

object AsyncDbManager {

  val mongoClient = MongoClient("mongodb://login-guest-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk")
  val database = mongoClient.getDatabase("heroku_3bppsqjk")
  val collection = database.getCollection("casual-queue")
  val documentId = "casual-queue"

  val filter = Filters.equal("_id", value = documentId)

  def findPlayerInQueue: Future[Document] = {
    collection.findOneAndDelete(filter).head()
  }

  def putPlayerInQueue(playerName: String,teamMembers: Seq[String],replyTo: String): Unit = {
    collection.insertOne(Document("_id" -> "casual-queue", "playerName" -> playerName, "teamMembers" -> teamMembers,"replytoQueue" -> replyTo))
  }


}
