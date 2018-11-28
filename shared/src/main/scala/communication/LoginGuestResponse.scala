package communication

/** The response message that the client will receive after a LoginGuestRequest.
  *
  * @param guestId the incremental ID assigned to the guest
  * @param details eventual details about the response that the server might attach for the client
  * @author Marco Canducci
  */
case class LoginGuestResponse(guestId: Option[Int], details: Option[String])
