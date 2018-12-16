package turnordering

import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
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
      .find(filter = Filters.equal("_id", battleId))
      .map(document => document(ActiveBattles.CurrentRound).asInt32().getValue)
      .head() // returns the Observable's head in a Future
  }

  /** Increments the current round of a given active battle.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation
    */
  def incrementCurrentRound(battleId: String): Future[UpdateResult] = {
    val incByOne: Bson = inc(ActiveBattles.CurrentRound, number = 1)

    battlesCollection
      .updateOne(filter = Filters.equal("_id", battleId), update = incByOne)
      .head()
  }

  /** Add all the player's informations for the turn ordering of the next round.
    *
    * @param playerInfo the player informations
    * @return a Future representing the success/failure of the operation
    */
  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = ???

  def getPlayerInfo(playerName: String, battleId: String, round: Int): Future[PlayerInfo] = ???

  def deletePlayerInfo(playerName: String, battleId: String, round: Int): Future[DeleteResult] = ???

  private object ManagerHelper {

    def playerInfoToDocument(playerInfo: PlayerInfo): Document = ???


  }

}

object ManagerTesting extends App {
  import MongoDbManager._


  getCurrentRound(battleId = "1-2").onComplete {
    case Success(round) => println("Success: " + round)
    case Failure(e)     => println(s"Failure: $e")
  }

  Thread.sleep(3000)

  incrementCurrentRound(battleId = "1-2").onComplete{
    case Success(success) => println("Success: " + success)
    case Failure(e)     => println(s"Failure: $e")
  }

  Thread.sleep(3000)

  getCurrentRound(battleId = "1-2").onComplete {
    case Success(round) => println("Success: " + round)
    case Failure(e)     => println(s"Failure: $e")
  }

  Thread.sleep(3000)

}
