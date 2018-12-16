package communication

import model.{Move, Character}

case class StatusUpdateMessage(newStatuses: Move.NewStatuses, round: Int, turn: Character)
