package model

case class Modifier(var affectedStat: String, var turnDuration: Int, var modValue: Int) {

  def decreaseDuration(): Unit = {
    turnDuration = turnDuration - 1
  }
}
