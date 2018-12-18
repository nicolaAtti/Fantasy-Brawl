package communication

import communication.StatusUpdateMessage.CharacterKey
import model.Status

/**The message that the clients exchange with each other.
  * Every client sends this message to notify his opponent about his move.
  *
  * @param attacker the character that makes the move
  * @param moveName the move that the character makes
  * @param targets the characters targets of the move
  * @param newStatuses all the statuses that were been modified by the move
  * @param round the current round
  */
case class StatusUpdateMessage(attacker: CharacterKey,
                               moveName: String,
                               targets: Set[CharacterKey],
                               newStatuses: Map[CharacterKey, Status],
                               round: Int)

object StatusUpdateMessage {
  type PlayerName = String
  type CharacterName = String
  type CharacterKey = (PlayerName, CharacterName)
}
