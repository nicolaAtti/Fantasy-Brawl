package model

/**
  * A traid defining an object that will expire after a set number of turns
  *
  * @author Nicola Atti
  */
trait Timed {

  var turnDuration: Int

  /**
    * Reduces the turnDuration by one
    */
  def decreaseDuration(): Unit = {
    turnDuration = turnDuration - 1
  }

  /**
    * Resets the object to it's maximum duration
    * @param duration
    */
  def resetDuration(duration: Int): Unit = {
    turnDuration = duration
  }

}

/**
  * Class implementing afflictons
  * @param afflictionType  defines the type of affliction between the set types (Asleep,Stunned,Frozen,Poisoned,Blinded,Silenced,Berserk)
  * @param turnDuration determines the duration in turns of the affliction, when it reaches 0 it no longer has an effect
  * @author Nicola Atti
  */
case class Affliction(var afflictionType: String, override var turnDuration: Int) extends Timed

/**
  * Class implementing modifiers
  * @param modifierId  modifier unique name to prevent stacking
  * @param affectedStatistic
  * @param turnDuration
  * @param modifierValue
  * @author Nicola Atti
  */
case class Modifier(var modifierId: String, var affectedStatistic: String, override var turnDuration: Int, var modifierValue: Int)
    extends Timed
