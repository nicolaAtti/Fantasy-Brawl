package model




trait Character {

  /**
    * The character's name
    */
  var charName: String
  /**
    * The character's base statistics
    */
  var stats: Statistics
  /**
    * The character's class multipliers
    */
  var classMods: StatModifiers

  import utilities.Utility._

  /**
    * The character's maximum HP
    */
  val maxHP: Int = roundDown((stats.resistance * classMods.hpMod) * 10)
  /**
    * The character's maximum MP
    */
  val maxMP: Int = roundDown((stats.spirit * classMods.spiMod) * 5)

  import scala.collection.mutable._
  /**
    * The list containing the character's active afflictions
    */
  var afflictions: MutableList[Affliction] = MutableList()
  /**
    * The list containing the character's active modifiers
    */
  var statMods: MutableList[Modifier] = MutableList()



  /**
    * Calculates the character's physical damage
    * @return
    */
  def getPhysDamage: Int = roundDown(stats.strength * classMods.strMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus physical critical strike damage
    * @return
    */
  def getPysCritDamage: Int = roundDown((stats.strength * classMods.strMod) / 5 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's speed
    * @return
    */
  def getSpeed: Int = roundDown((stats.agility * classMods.agiMod) / 10) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's critical strike chance
    * @return
    */
  def getCritChance: Int = roundDown((stats.agility * classMods.agiMod) / 2) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical defence
    * @return
    */
  def getMagDefence: Int = roundDown(stats.spirit * classMods.spiMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical damage
    * @return
    */
  def getMagDamage: Int = roundDown(stats.intelligence * classMods.intMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus magical critical strike damage
    * @return
    */
  def getMagicCritDamage: Int = roundDown((stats.intelligence * classMods.intMod) / 10 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's physical defence
    * @return
    */
  def getPhysDefence: Int = roundDown(stats.resistance * classMods.resMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * The character's current HP
    */
  var currHP: Int = maxHP
  /**
    * The character's current MP
    */
  var currMP: Int = maxMP

  //Passagli la funzione della mossa, sia essa speciale, attacco base o passare il turno   STRATEGY
  //Forse conviene mettere qui il controllo per le afflizzioni che limitano le mosse tipo: if stunnato passa solo, if ghiacciato no mosse meelee etc
  /// TODO:
  def doMove = ???

  /**
    * Adds a new modifier to the character, if the same modifier is already present refresh it's duration to the new one
    * @param newMod the new modifier
    * @author Nicola Atti
    */
  def addModifier(newMod: Modifier): Unit = {
    if(statMods.exists(mod => mod.modId equals newMod.modId)) {
      statMods.filter(mod => mod.modId equals newMod.modId).foreach(existingMod => existingMod.resetDuration(newMod.turnDuration))
    }else statMods += newMod
  }

  /**
    * Adds a new affliction to the character
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
    statMods foreach (modifier => modifier.decreaseDuration())
      statMods = statMods.filter(mod => mod.turnDuration <= 0)
    if (afflictions.exists(affl => affl.afflictionType equals "Poisoned")) {
      changeCurrHPMP("HP", sub, maxHP / 4)
    }
    afflictions foreach (affliction => affliction.decreaseDuration())
    afflictions = afflictions.filter(affl => affl.turnDuration <= 0)
  }

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    * @param affectedStat the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    * @author Nicola Atti
    */
  def getModifierValues(affectedStat: String): Int = {
    var allModsValue = 0
    statMods foreach (modifier => allModsValue = add(allModsValue, modifier.modValue))
    allModsValue
  }

  /**
    * Changes the character current HP and MP values (adding or subtracting)
    * @param stat  the statistic to modify
    * @param function defines the behaviour of the function
    * @param value the value to add or subtract
    * @author Nicola Atti
    */
  def changeCurrHPMP(stat: String,
                     function: (Int, Int) => Int,
                     value: Int): Unit = stat.toUpperCase match {
    case "HP" =>
      if (function(currHP, value) > maxHP) {
        currHP = maxHP
      } else if (function(currHP, value) < 0) {
        currHP = 0
      } else {
        currHP = function(currHP, value)
      }
    case "MP" =>
      if (function(currMP, value) > maxMP) {
        currMP = maxMP
      } else if (function(currMP, value) < 0) {
        currMP = 0
      } else {
        currMP = function(currMP, value)
      }
  }
}

/**
  * Default implementation of a game Character
  *
  * @param charName  the name of the Character
  * @param stats     the statistics of the Character
  * @param classMods the class statistic modifier, this will vary from Character implementation
  * @author Nicola Atti
  */
private case class Warrior(override var charName: String,
                           override var stats: Statistics,
                           override var classMods: StatModifiers =
                           StatModifiers(2, 1, 1, 0.5, 1.5, 2))
  extends Character {}

private case class Thief(override var charName: String,
                         override var stats: Statistics,
                         override var classMods: StatModifiers =
                         StatModifiers(1.5, 2, 1, 0.5, 1, 1.5))
  extends Character {}

private case class Wizard(override var charName: String,
                          override var stats: Statistics,
                          override var classMods: StatModifiers =
                          StatModifiers(1, 1, 1.5, 2, 0.5, 1.5))
  extends Character {}

private case class Healer(override var charName: String,
                          override var stats: Statistics,
                          override var classMods: StatModifiers =
                          StatModifiers(1.5, 0.5, 1, 2, 1, 1.5))
  extends Character {}


object Character {
  def apply(role: String, charName: String, stats: Statistics): Character =
    role match {
      case "Warrior" => Warrior(charName, stats)
      case "Thief" => Thief(charName, stats)
      case "Wizard" => Wizard(charName, stats)
      case "Healer" => Healer(charName, stats)
    }

}
