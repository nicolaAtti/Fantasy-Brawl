package matchmaking

import communication.matchmaking.PlayerInfo
import scala.concurrent.Future

/** Provides functionality to manage the asynchronous interaction between the
  * matchmaking service and an asynchronous database.
  *
  * @author Nicola Atti, Marco Canducci
  */
trait AsyncDbManager {

  /** Retrieves a ticket from the casual queue and increments the ticket counter.
    *
    * @return a Future representing the success/failure of the operation,
    *         containing the casual queue's ticket number
    */
  def getTicket: Future[Int]

  /** Creates a document representing a new player inside the casual queue.
    *
    * @param ticket the player's ticket for the matchmaking queue
    * @param playerInfo the player's information
    * @param replyTo the player response queue
    * @return a Future representing the success/failure of the operation,
    *         containing a string with a brief summary of the update
    */
  def putPlayerInQueue(ticket: Int, playerInfo: PlayerInfo, replyTo: String): Future[String]

  /** Searches for the player information associated with the given ticket and,
    * if present, removes them.
    *
    * @return a Future representing the success/failure of the operation inside
    *         an optional Some/None, containing, if successful, the player's
    *         information, the response queue and a flag that represents if the
    *         player has left the queue or not.
    */
  def takePlayerFromQueue(ticket: Int): Future[Option[(PlayerInfo, String, Boolean)]]

  /** Removes the existing document of a queued player given its name.
    *
    * @param playerName the player to remove
    * @return a Future representing the success/failure of the operation,
    *         containing true if the player is found and removed, false otherwise.
    */
  def removePlayerFromQueue(playerName: String): Future[Boolean]

  /** Notifies that a player has left the game while waiting for an opponent.
    *
    * @param playerName the name of the player that left the queue
    * @return a Future representing the success/failure of the operation,
    *         containing true if the player info are found and updated,
    *         false otherwise.
    */
  def notifyPlayerLeft(playerName: String): Future[Boolean]

  /** Creates a new battle instance inside the battles collection.
    *
    * @param battleId the battle identifier
    * @return a Future representing the success/failure of the operation,
    *         containing a string with a brief summary of the update
    */
  def createBattleInstance(battleId: String): Future[String]

}
