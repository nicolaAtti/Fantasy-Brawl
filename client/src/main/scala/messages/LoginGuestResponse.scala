package messages

case class LoginGuestResponse(guestId: Option[Int], details: Option[String])
