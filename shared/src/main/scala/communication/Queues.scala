package communication

/** All the queues that connect the services to themselves and to the client. */
object Queues {
  def uuid = java.util.UUID.randomUUID.toString

  val LoginGuestRequestQueue = "login-guest-request"
  val LoginGuestResponseQueue = uuid
}
