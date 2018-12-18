package communication.matchmaking

/** The player info about his/her matchmaking request.
  *
  * @param name the player name
  * @param teamNames the player team characters' names
  * @param battleQueue the player's battle response queue
  */
case class PlayerInfo(name: String, teamNames: Set[String], battleQueue: String)
