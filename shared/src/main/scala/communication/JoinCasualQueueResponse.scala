package communication

import communication.JoinCasualQueueResponse.BattleId
import communication.matchmaking.PlayerInfo

/** The response message that the client will receive after an opponent is chosen
  * from the matchmaking service.
  *
  * @param opponentData Right((player, battleId)) player and his battle id,
  *                     Left(error) details about the error
  */
case class JoinCasualQueueResponse(opponentData: Either[String, (PlayerInfo, BattleId)])

object JoinCasualQueueResponse {
  type BattleId = String
}
