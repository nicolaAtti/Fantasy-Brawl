package turnordering

case class PlayerInfo (playerName: String,
                       playerTeamSpeeds: Map[String, Int],
                       battleId: String,
                       round: String,
                       replyTo: String)
