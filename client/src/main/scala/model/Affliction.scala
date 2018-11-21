package model

case class Affliction(var afflictionType: String, var turnDuration: Int, var damageValue: Option[Int]) {

  def decreaseDuration(): Unit = {
    turnDuration = turnDuration - 1
  }
}
