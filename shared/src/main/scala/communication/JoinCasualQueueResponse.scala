package communication

/** The response message that the client will receive after an opponent is chosen from the matchmaking service.
  *
  * @param opponentData team with which the opponent player wants to fight.
  */
case class JoinCasualQueueResponse(opponentData: Either[String, (String, Set[String], String)])
