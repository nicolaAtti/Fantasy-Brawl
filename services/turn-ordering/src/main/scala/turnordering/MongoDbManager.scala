package turnordering

import communication.turnordering.PlayerInfo
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

/** Provides functionality to manage the asynchronous interaction between the
  * turn-ordering service and a MongoDB database.
  *
  * @author Nicola Atti, Marco Canducci
  */
object MongoDbManager extends AsyncDbManager {

  import config.DbNaming._

  val database = MongoClient(ClientName).getDatabase(DatabaseName)
  val battlesCollection = database.getCollection(Battles.CollectionName)
  val turnOrderingCollection = database.getCollection(TurnOrdering.CollectionName)

  def getCurrentRound(battleId: String): Future[Int] = {
    battlesCollection
      .find(filter = Filters.equal("_id", battleId))
      .map(document => document(Battles.CurrentRound).asInt32().getValue)
      .head
  }

  def incrementCurrentRound(battleId: String): Future[Boolean] = {
    val incByOne: Bson = inc(Battles.CurrentRound, number = 1)
    battlesCollection
      .updateOne(filter = Filters.equal("_id", battleId), update = incByOne)
      .map(updateResult => updateResult.getModifiedCount > 0)
      .head
  }

  import DbHelper._

  def addPlayerInfo(playerInfo: PlayerInfo): Future[String] = {
    turnOrderingCollection
      .insertOne(playerInfo)
      .map(_ => s"Successfully added ${playerInfo.name} information.")
      .head
  }

  def getPlayerInfo(playerName: String, battleId: String, round: Int): Future[Option[PlayerInfo]] = {
    turnOrderingCollection
      .find(filter = Filters.equal(TurnOrdering.PlayerName, playerName))
      .first
      .map(documentToPlayerInfo)
      .headOption
  }

  def deletePlayerInfo(playerName: String, battleId: String, round: Int): Future[Boolean] = {
    turnOrderingCollection
      .deleteOne(
        filter = Filters.and(Filters.equal(TurnOrdering.PlayerName, playerName),
                             Filters.equal(TurnOrdering.BattleId, battleId),
                             Filters.equal(TurnOrdering.Round, round)))
      .map(deleteResult => deleteResult.getDeletedCount > 0)
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

/** Naive test useful both as a usage example and as a verification for the
  * conversion from PlayerInfo to Document and vice-versa.
  * (NB: Test only when the remote Database is online!)
  */
object TestConversion extends App {

  import MongoDbManager._

  val playerName = "Mark999"
  val speeds = Map("Annabelle" -> 8, "Jacob" -> 5, "Fernando" -> 6)
  val battleId = "88-89"
  val round = 15
  val clientQueue = "fkjghsdfkjghsdflkjdhlfgoielrjglkdjhfwoghsldgfjasd"

  val playerInfo =
    PlayerInfo(name = playerName, teamSpeeds = speeds, battleId = battleId, round = round, replyTo = clientQueue)

  import config.MiscSettings._

  addPlayerInfo(playerInfo).onComplete {

    case Success(_) =>
      println("Player info successfully added to the Database")
      getPlayerInfo(playerName, battleId, round).onComplete {

        case Success(retrievedInfo) =>
          println("Player info retrieved from the Database")
          println(s"--> Original:  $playerInfo")
          println(s"--> Retrieved: $retrievedInfo")
          println(s"--> Have I done a good job? ${playerInfo == retrievedInfo}")
          deletePlayerInfo(playerName, battleId, round).onComplete {

            case Success(_) => println("Player info successfully deleted from the Database")

            case Failure(e) => println(s"$LogFailurePrefix$e")
          }
        case Failure(e) => println(s"$LogFailurePrefix$e")
      }
    case Failure(e) => println(s"$LogFailurePrefix$e")
  }

  Thread.sleep(10000) // wait for async requests to take place
}
