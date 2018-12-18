package communication

import communication.StatusUpdateMessage.CharacterKey
import model.Status

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
