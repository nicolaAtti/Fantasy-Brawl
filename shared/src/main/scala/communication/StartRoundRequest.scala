package communication

case class StartRoundRequest(playerName: String,
                             playerTeamSpeeds: Map[String, Int],
                             opponentName: String,
                             battleId: String,
                             round: Int)
