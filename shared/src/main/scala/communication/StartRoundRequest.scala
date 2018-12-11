package communication

case class StartRoundRequest(playerName: String, myTeam: Map[String, Int], round: Int)
