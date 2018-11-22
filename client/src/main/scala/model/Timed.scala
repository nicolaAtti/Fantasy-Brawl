package model

trait Timed {

  var turnDuration: Int

  def decreaseDuration(): Unit = {
    turnDuration = turnDuration - 1
  }

  def resetDuration(duration: Int): Unit = {
    turnDuration = duration
  }

}

case class Affliction(var afflictionType: String, override var turnDuration: Int) extends Timed {
}


case class Modifier(var modId: String, var affectedStat: String, override var turnDuration: Int, var modValue: Int) extends Timed {
}
