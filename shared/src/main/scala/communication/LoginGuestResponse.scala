package communication

/** The response message that the client will receive after a LoginGuestRequest.
  *
  * @param guestId Right(id) the incremental ID assigned to the guest,
  *                Left(error) details about the error
  * @author Marco Canducci
  */
case class LoginGuestResponse(guestId: Either[String, Int])
