package communication

/** The request message that clients must send if they want to login as guests.
  *
  * @param details possible details that the client could want to attach.
  * @author Marco Canducci
  */
case class JoinCasualQueueRequest(playerName: String, team: Map[String, String])