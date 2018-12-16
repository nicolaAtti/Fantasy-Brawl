package turnordering

import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase}

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global

object MongoDbManager {

  import config.DbNaming._
  val database: MongoDatabase = MongoClient(ClientName).getDatabase(DatabaseName)
  val battlesCollection: MongoCollection[Document] = database.getCollection(ActiveBattles.CollectionName)

  /** Retrieves the current round of an ongoing battle.
    *
    * @param battleId the ongoing battle to check
    * @return a Future representing the success/failure of the operation,
    *         containing the current round number
    */
  def getCurrentRound(battleId: String): Future[Int] = {
    battlesCollection
      .find(filter = Filters.equal(fieldName = "_id", value = battleId))
      .map(document => document(ActiveBattles.CurrentRound).asInt32().getValue)
      .head() // returns the Observable's head in a Future
  }

  def incrementCurrentRound(battleId: String): Future[Completed] = ???

  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = ???

  def getPlayerInfo(playerName: String, battleId: String): Future[PlayerInfo] = ???

  def deletePlayerInfo(playerName: String, battleId: String): Future[DeleteResult] = ???

}

object ManagerTesting extends App {
  import MongoDbManager._

  getCurrentRound(battleId = "1-2").onComplete {
    case Success(round) => println("Success: "+round)
    case Failure(e)     => println(s"Failure: $e")
  }

  Thread.sleep(4000)

}
