package communication

object Config {
  val Qos: Int = 1
  val Requeue: Boolean = false
  val Durable: Boolean = false
  val AutoDelete: Boolean = true

  val ServicesLog: Boolean = true

  val MatchmakingAddKey: String = "add"
  val MatchmakingRemoveKey: String = "remove"

  val GuestName: String = "guest#"

  val TurnDurationInSeconds: Int = 60
}
