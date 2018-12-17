package communication

import communication.StatusUpdateMessage.CharacterKey

case class StatusUpdateMessage(attacker: CharacterKey, moveName: String, targets: Set[CharacterKey], round: Int)

object StatusUpdateMessage{
  type PlayerName = String
  type CharacterName = String
  type CharacterKey = (PlayerName, CharacterName)
}
