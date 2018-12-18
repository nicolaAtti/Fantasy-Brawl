package turnordering

import org.mongodb.scala.bson.BsonValue
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Updates.inc
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, MongoClient}

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global

object MongoDbManager {

  import config.DbNaming._

  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val battlesCollection = database.getCollection(Battles.CollectionName)
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
      .map(document => document(Battles.CurrentRound).asInt32().getValue)
      .head
  }

  /** Increments the current round of a given active battle.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation
    */
  def incrementCurrentRound(battleId: String): Future[UpdateResult] = {
    val incByOne: Bson = inc(Battles.CurrentRound, number = 1)
    battlesCollection
      .updateOne(filter = Filters.equal("_id", battleId), update = incByOne)
      .head
  }

  import DbHelper._

  /** Adds all the player's informations for the next round turn-ordering request.
    *
    * @param playerInfo the player informations
    * @return a Future representing the success/failure of the operation
    */
  def addPlayerInfo(playerInfo: PlayerInfo): Future[Completed] = {
    turnOrderingCollection
      .insertOne(playerInfo)
      .head
  }

  /** Retrieves all the player's informations of an old turn-ordering request.
    *
    * @param playerName the player name of the old request
    * @param battleId   the battle id of the old request
    * @param round      the round of the old request
    * @return a Future representing the success/failure of the operation
    */
  def getPlayerInfo(playerName: String, battleId: String, round: Int): Future[PlayerInfo] = {
    turnOrderingCollection
      .find(filter = Filters.equal(TurnOrdering.PlayerName, playerName))
      .first
      .map(documentToPlayerInfo)
      .head
  }

  /** Deletes the player's informations of an old turn-ordering request.
    *
    * @param playerName the player name of the old request
    * @param battleId   the battle id of the old request
    * @param round      the round of the old request
    * @return a Future representing the success/failure of the operation
    */
  def deletePlayerInfo(playerName: String, battleId: String, round: Int): Future[DeleteResult] = {
    turnOrderingCollection
      .deleteOne(
        filter = Filters.and(Filters.equal(TurnOrdering.PlayerName, playerName),
                             Filters.equal(TurnOrdering.BattleId, battleId),
                             Filters.equal(TurnOrdering.Round, round)))
      .head
  }

  private object DbHelper {

    /** Implicitly converts a PlayerInfo object to a MongoDb immutable Document.
      *
      * @param playerInfo the PlayerInfo object to convert
      * @return the mongodb immutable Document
      */
    implicit def playerInfoToDocument(playerInfo: PlayerInfo): Document = {
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

    /** Implicitly converts a MongoDb immutable Document to a PlayerInfo object.
      *
      * @param document the mongodb immutable Document
      * @return the PlayerInfo object
      */
    implicit def documentToPlayerInfo(document: Document): PlayerInfo = {
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

/** Very naive test useful both as a usage example and as a verification for the
  * conversion from PlayerInfo to Document and vice-versa.
  * (NB: Test only when the remote Database is online!)
  */
object TestConversion extends App {

  import MongoDbManager._

  val playerName = "Mark999"
  val speeds = Map("Annabelle" -> 8, "Jacob" -> 5, "Fernando" -> 6)
  val battleId = "88-89"
  val round = 15
  val clientQueue = "fkjghsdfkjghsdflkjdhlfgoielrjglkdjhfwoghsldgfj"

  val playerInfo =
    PlayerInfo(name = playerName, teamSpeeds = speeds, battleId = battleId, round = round, replyTo = clientQueue)

  import config.MiscSettings._

  addPlayerInfo(playerInfo).onComplete {

    case Success(_) => {
      println("Player info successfully added to the Database")
      getPlayerInfo(playerName, battleId, round).onComplete {

        case Success(retrievedInfo) => {
          println("Player info retrieved from the Database")
          println(s"--> Original:  $playerInfo")
          println(s"--> Retrieved: $retrievedInfo")
          println(s"--> Have I done a good job? ${playerInfo == retrievedInfo}")
          deletePlayerInfo(playerName, battleId, round).onComplete {

            case Success(_) => println("Player info successfully deleted from the Database")

            case Failure(e) => println(s"$LogFailurePrefix$e")
          }
        }
        case Failure(e) => println(s"$LogFailurePrefix$e")
      }
    }
    case Failure(e) => println(s"$LogFailurePrefix$e")
  }

  Thread.sleep(10000) // wait for async requests to take place
}
