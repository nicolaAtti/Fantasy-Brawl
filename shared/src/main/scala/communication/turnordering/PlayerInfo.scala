package communication.turnordering

/** The player information about his/her turn-ordering request.
  *
  * @param name the player name
  * @param teamSpeeds a map with the team characters' names and the relative speeds
  * @param battleId the battle identifier
  * @param round the round that the request refers to
  * @param replyTo the player's turn-ordering response queue
  */
case class PlayerInfo(name: String, teamSpeeds: Map[String, Int], battleId: String, round: Int, replyTo: String)
