package model

import utilities.Utility._
import scala.collection.mutable._

case class Status(var currentHP: Int,
                  var currentMP: Int,
                  var modifiers: MutableList[Modifier],
                  var afflictions: MutableList[Affliction]) {

  val maxHP = currentHP
  val maxMP = currentMP

  /**
    * Adds a new modifier to the character, if the same modifier is already present refresh it's duration to the new one
    * @param newMod the new modifier
    * @author Nicola Atti
    */
  def addModifier(newMod: Modifier): Unit = {
    if (!modifiers.exists(mod => mod.modifierId equals newMod.modifierId)) {
      modifiers += newMod
    } else {
      modifiers
        .filter(_.modifierId equals newMod.modifierId)
        .foreach(matchingMod => matchingMod.resetDuration(newMod.turnDuration))
    }
  }

  /**
    * Adds a new affliction to the character, if it isn't already one of the same type
    * @param newAffl the new affliction
    * @author Nicola Atti
    */
  def addAffliction(newAffl: Affliction): Unit = {
    if (!afflictions.exists(affl => affl.afflictionType equals newAffl.afflictionType)) {
      afflictions += newAffl
    }
  }

  /**
    * Reduces the turnDuration of every affliction and modifier of the character,
    * removing the ones that reach zero, if the character is Poisond apply the damage
    * @author Nicola Atti
    */
  def newTurnCountdown(): Unit = {
    modifiers foreach (modifier => modifier.decreaseDuration())
    modifiers = modifiers.filterNot(mod => mod.turnDuration == 0)

    if (afflictions.exists(affl => affl.afflictionType equals "Poisoned")) {
      changeCurrHPMP("HP", sub, currentHP / 4)
    }
    afflictions foreach (affliction => affliction.decreaseDuration())
    afflictions = afflictions.filterNot(affl => affl.turnDuration == 0)
  }

  /**
    * Changes the character current HP and MP values (adding or subtracting)
    * @param stat  the statistic to modify
    * @param function defines the behaviour of the function
    * @param value the value to add or subtract
    * @author Nicola Atti
    */
  def changeCurrHPMP(stat: String, function: (Int, Int) => Int, value: Int): Unit = stat.toUpperCase match {
    case "HP" =>
      if (function(currentHP, value) > maxHP) {
        currentHP = maxHP
      } else if (function(currentHP, value) < 0) {
        currentHP = 0
      } else {
        currentHP = function(currentHP, value)
      }
    case "MP" =>
      if (function(currentMP, value) > maxMP) {
        currentMP = maxMP
      } else if (function(currentMP, value) < 0) {
        currentMP = 0
      } else {
        currentMP = function(currentMP, value)
      }
  }
}
