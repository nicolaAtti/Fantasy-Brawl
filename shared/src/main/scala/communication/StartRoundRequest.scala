package communication

case class StartRoundRequest(playerName: String, myTeamSpeeds: Map[String, Int], round: Int)
