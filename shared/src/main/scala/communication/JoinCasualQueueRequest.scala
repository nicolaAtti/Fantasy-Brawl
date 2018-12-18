package communication

import communication.JoinCasualQueueRequest.Operation
import communication.matchmaking.PlayerInfo

/** The request message that clients must send if they want to join a casual queue
  * in the matchmaking system.
  *
  * @param player the player that wants to join.
  * @param operation add or remove to/from matchmaking
  */
case class JoinCasualQueueRequest(player: PlayerInfo, operation: Operation)

object JoinCasualQueueRequest {
  object Operation extends Enumeration {
    val ADD, REMOVE = Value
  }
  type Operation = Operation.Value
}
