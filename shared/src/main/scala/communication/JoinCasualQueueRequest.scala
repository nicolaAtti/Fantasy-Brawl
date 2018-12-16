package communication

/** The request message that clients must send if they want to join a casual queue in the matchmaking system.
  *
  * @param playerName username of the player that wants to join.
  * @param team team with which the player wants to fight.
  * @param operation add or remove to/from matchmaking
  * @param battleQueue player queue used to exchange battle update messages
  */
case class JoinCasualQueueRequest(playerName: String, team: Set[String], operation: String, battleQueue: String)
