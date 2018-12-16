package turnordering

import org.mongodb.scala.bson.{BsonArray, BsonValue}
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
  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val battlesCollection = database.getCollection(ActiveBattles.CollectionName)
  val turnOrderingCollection = database.getCollection(TurnOrdering.CollectionName)

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
      .head
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
      .head
  }

  /** Add all the player's informations for the turn ordering of the next round.
    *
    * @param playerInfo the player informations
    * @return a Future representing the success/failure of the operation
    */
  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = {
    turnOrderingCollection
      .insertOne(ManagerHelper.playerInfoToDocument(playerInfo))
      .head
  }

  def getPlayerInfo(playerName: String, battleId: String, round: Int): Future[PlayerInfo] = {
    turnOrderingCollection
      .find(filter = Filters.equal(TurnOrdering.PlayerName, playerName))
      .first
      .map(ManagerHelper.documentToPlayerInfo)
      .head
  }

  def deletePlayerInfo(playerName: String, battleId: String, round: Int): Future[DeleteResult] = {
    turnOrderingCollection
      .deleteOne(
        filter = Filters.and(Filters.equal(TurnOrdering.PlayerName, playerName),
                             Filters.equal(TurnOrdering.BattleId, battleId),
                             Filters.equal(TurnOrdering.Round, round)))
      .head
  }

  object ManagerHelper {

    def playerInfoToDocument(playerInfo: PlayerInfo): Document = {
      val (characterNames: List[String], characterSpeeds: List[Int]) = playerInfo.teamSpeeds.unzip

      Document(
        TurnOrdering.PlayerName -> playerInfo.name,
        TurnOrdering.BattleId -> playerInfo.battleId,
        TurnOrdering.Round -> playerInfo.round,
        TurnOrdering.TeamNames -> characterNames,
        TurnOrdering.TeamSpeedValues -> characterSpeeds,
        TurnOrdering.ReplyTo -> playerInfo.replyTo
      )
    }

    def documentToPlayerInfo(document: Document): PlayerInfo = {
      val teamNames: Array[String] = document(TurnOrdering.TeamNames).asArray.toArray.map {
        case bsonName: BsonValue => bsonName.asString.getValue
      }

      val teamSpeedValues: Seq[Int] = document(TurnOrdering.TeamSpeedValues).asArray.toArray.map {
        case bsonSpeed: BsonValue => bsonSpeed.asInt32.getValue
      }

      val zippedSpeeds = teamNames.zip(teamSpeedValues)

      PlayerInfo(
        name = document(TurnOrdering.PlayerName).asString.getValue,
        teamSpeeds = zippedSpeeds.toMap,
        battleId = document(TurnOrdering.BattleId).asString.getValue,
        round = document(TurnOrdering.Round).asInt32.getValue,
        replyTo = document(TurnOrdering.ReplyTo).asString.getValue
      )
    }
  }

}

object TestConversion extends App {
  import MongoDbManager._

  val playerInfo = PlayerInfo("Nome1", Map("Marco" -> 10, "Nicola" -> 9), "id/battle", 0, "uuidrepr6")

  addPlayerInfo(playerInfo)

  getPlayerInfo("Nome1", "id/battle", 0).onComplete {
    case Success(info) => println(info + "\n" + playerInfo + "\n" + (info == playerInfo))
    case Failure(e)    => println(s"error $e")
  }

  Thread.sleep(5000)

}
