package communication

case class StartRoundRequest(playerName: String,
                             myTeamSpeeds: Map[String, Int],
                             opponentName: String,
                             battleId: String,
                             round: Int)
