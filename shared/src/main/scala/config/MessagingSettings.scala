package config

object MessagingSettings {

  // op-rabbit
  val Qos: Int = 1
  val Requeue: Boolean = false
  val Durable: Boolean = false
  val AutoDelete: Boolean = true

  // message codes
  val PlayerJoinedCasualQueue: String = "player-joined"
  val PlayerLeftCasualQueue: String = "player-left"

}
