package communication

import communication.StatusUpdateMessage.CharacterKey

case class StatusUpdateMessage(character: CharacterKey, moveName: String, targets: Set[CharacterKey], round: Int)

object StatusUpdateMessage{
  type PlayerName = String
  type CharacterName = String
  type CharacterKey = (PlayerName, CharacterName)
}
