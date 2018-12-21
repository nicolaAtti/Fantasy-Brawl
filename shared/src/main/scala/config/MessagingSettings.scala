package config

/** Settings for op-rabbit queue messaging */
object MessagingSettings {

  val Qos: Int = 1
  val Requeue: Boolean = false
  val Durable: Boolean = false
  val AutoDelete: Boolean = true
}
