package communication

/** The request message that the clients send to start a new round, asking to obtain
  * the ordered turns' list.
  *
  * @param playerName the name of the player who made the request
  * @param playerTeamSpeeds team's speeds of the player
  * @param opponentName the opponent name
  * @param battleId the battle identifier
  * @param round the round that the player wants to start
  */
case class StartRoundRequest(playerName: String,
                             playerTeamSpeeds: Map[String, Int],
                             opponentName: String,
                             battleId: String,
                             round: Int)
