package communication

import model.{Move, Character}

case class StatusUpdateMessage(newStatus: Move.NewStatuses, round: Int, turn: Character)
