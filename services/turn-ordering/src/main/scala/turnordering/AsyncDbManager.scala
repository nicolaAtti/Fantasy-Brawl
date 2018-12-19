package turnordering

import scala.concurrent.Future
import communication.turnordering.PlayerInfo

/** Provides functionality to manage the asynchronous interaction between the
  * turn-ordering service and an asynchronous database.
  *
  * @author Nicola Atti, Marco Canducci
  */
trait AsyncDbManager {

  /** Retrieves the current round of an ongoing battle.
    *
    * @param battleId the ongoing battle identifier to check
    * @return a Future representing the success/failure of the operation,
    *         containing the current round number
    */
  def getCurrentRound(battleId: String): Future[Int]

  /** Increments by one the current round of a given active battle.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation,
    *         containing true if the current round counter is incremented,
    *         false otherwise.
    */
  def incrementCurrentRound(battleId: String): Future[Boolean]

  /** Adds all the player's information for a turn-ordering request.
    *
    * @param playerInfo the player information
    * @return a Future representing the success/failure of the operation,
    *         containing a string with a brief summary of the update
    */
  def addPlayerInfo(playerInfo: PlayerInfo): Future[String]

  /** Retrieves all the player's information relative to an old turn-ordering request.
    *
    * @param playerName the player name of the old request
    * @param battleId the battle identifier of the old request
    * @param round the round of the old request
    * @return a Future representing the success/failure of the operation,
    *         containing an optional Success(Some(playerInfo)) if the player
    *         information are found, Success(None) otherwise
    */
  def getPlayerInfo(playerName: String, battleId: String, round: Int): Future[Option[PlayerInfo]]

  /** Deletes the player's information of an old turn-ordering request.
    *
    * @param playerName the player name of the old request
    * @param battleId the battle id of the old request
    * @param round the round of the old request
    * @return a Future representing the success/failure of the deletion,
    *         containing true if the player is found and deleted, false otherwise
    */
  def deletePlayerInfo(playerName: String, battleId: String, round: Int): Future[Boolean]

}
