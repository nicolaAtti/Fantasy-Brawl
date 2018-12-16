package turnordering

import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Future

object MongoDbManager {

  import config.DbNaming._
  val database: MongoDatabase = MongoClient(ClientName).getDatabase(DatabaseName)
  val battlesCollection = database.getCollection(ActiveBattles.CollectionName)

  def getCurrentRound(battleId: String): Future[Int] = ???

  def incrementCurrentRound(battleId: String): Future[Completed] = ???

  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = ???

  def getPlayerInfo(playerName: String, battleId: String): Future[PlayerInfo] = ???

  def deletePlayerInfo(playerName: String, battleId: String): Future[DeleteResult] = ???

}
