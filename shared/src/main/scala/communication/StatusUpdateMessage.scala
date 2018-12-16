package communication

import model.Move

case class StatusUpdateMessage(newStatus: Move.NewStatuses, round: Int)
