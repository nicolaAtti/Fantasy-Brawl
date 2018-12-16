package communication

/** All the queues that connect the services to themselves and to the client. */
object Queues {
  def uuid: String = java.util.UUID.randomUUID.toString

  val LoginGuestRequestQueue: String = "login-guest-request"
  val LoginGuestResponseQueue: String = uuid
  val JoinCasualMatchmakingRequestQueue: String = "casual-mm-request"
  val JoinCasualMatchmakingResponseQueue: String = uuid
  val StartRoundRequestQueue: String = "start-round-request"
  val StartRoundResponseQueue: String = uuid
  val BattleQueue: String = uuid

}
