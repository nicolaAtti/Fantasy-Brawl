package messages

/** The request message that clients must send if they want to login as guests.
  *
  * @param details possible details that the client could want to attach.
  */
case class LoginGuestRequest(details: Option[String])
