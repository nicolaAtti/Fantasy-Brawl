package turnordering

import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, MongoClient, MongoDatabase}

import scala.concurrent.Future

object MongoDbManager {

  import config._
  val database: MongoDatabase = MongoClient(DbNaming.ClientName).getDatabase(DbNaming.DatabaseName)

  def getCurrentRound(battleId: String): Future[Int] = ???

  def incrementCurrentRound(battleId: String): Future[Completed] = ???

  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = ???

  def getPlayerInfo(playerName: String, battleId: String): Future[PlayerInfo] = ???

  def deletePlayerInfo(playerName: String, battleId: String): Future[DeleteResult] = ???

}
