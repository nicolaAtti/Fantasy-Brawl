package model

trait Character {

  /**
    * The character's name
    */
  val characterName: String

  /**
    * The character's base statistics
    */
  val statistics: Statistics

  /**
    * The character's class multipliers
    */
  val classMultipliers: ClassStat

  import utilities.Utility._

  /**
    * The character's maximum HP
    */
  val maxHP: Int = roundDown((statistics.resistance * classMultipliers.hpMod) * 10)

  /**
    * The character's maximum MP
    */
  val maxMP: Int = roundDown((statistics.spirit * classMultipliers.spiritMod) * 5)

  import scala.collection.mutable._

  /**
    * Contains the character's status, depending on current HP and MP values and existing modifiers and afflictions
    */
  var status: Status = Status(maxHP, maxMP, MutableList(), MutableList())

  /**
    * Calculates the character's physical damage
    * @return
    */
  def calculatePhysicalDamage: Int = roundDown(statistics.strength * classMultipliers.strengthMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus physical critical strike damage
    * @return
    */
  def calculatePysicalCriticalDamage: Int =
    roundDown((statistics.strength * classMultipliers.strengthMod) / 5 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's speed
    * @return
    */
  def calculateSpeed: Int = roundDown((statistics.agility * classMultipliers.agilityMod) / 10) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's critical strike chance
    * @return
    */
  def calculateCriticalChance: Int = roundDown((statistics.agility * classMultipliers.agilityMod) / 2) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical defence
    * @return
    */
  def calculateMagicalDefence: Int = roundDown(statistics.spirit * classMultipliers.spiritMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical damage
    * @return
    */
  def calculateMagicalDamage: Int = roundDown(statistics.intelligence * classMultipliers.intelligenceMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus magical critical strike damage
    * @return
    */
  def calculateMagicalCriticalDamage: Int =
    roundDown((statistics.intelligence * classMultipliers.intelligenceMod) / 10 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's physical defence
    * @return
    */
  def calculatePhysicalDefence: Int = roundDown(statistics.resistance * classMultipliers.resistanceMod) + getModifierValues("PHYS_DAMAGE")

  //Passagli la funzione della mossa, sia essa speciale, attacco base o passare il turno   STRATEGY
  //Forse conviene mettere qui il controllo per le afflizzioni che limitano le mosse tipo: if stunnato passa solo, if ghiacciato no mosse meelee etc
  /// TODO:
  def doMove = ???

  /**
    * Resets the status of the character to the original values
    */
  def setStatus(newStatus: Status): Unit = {
    status = newStatus
  }

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    * @param affectedStat the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    * @author Nicola Atti
    */
  def getModifierValues(affectedStat: String): Int = {
    var allModsValue = 0
    status.modifiers foreach (modifier => allModsValue = add(allModsValue, modifier.modifierValue))
    allModsValue
  }

}

/**
  * Default implementation of a game Character
  *
  * @param characterName  the name of the Character
  * @param statistics     the statistics of the Character
  * @param classMultipliers the class statistic modifier, this will vary from Character implementation
  * @author Nicola Atti
  */
private case class Warrior(override val characterName: String,
                           override val statistics: Statistics,
                           override val classMultipliers: ClassStat = ClassStat(2, 1, 1, 0.5, 1.5, 2))
    extends Character {}

private case class Thief(override val characterName: String,
                         override val statistics: Statistics,
                         override val classMultipliers: ClassStat = ClassStat(1.5, 2, 1, 0.5, 1, 1.5))
    extends Character {}

private case class Wizard(override val characterName: String,
                          override val statistics: Statistics,
                          override val classMultipliers: ClassStat = ClassStat(1, 1, 1.5, 2, 0.5, 1.5))
    extends Character {}

private case class Healer(override val characterName: String,
                          override val statistics: Statistics,
                          override val classMultipliers: ClassStat = ClassStat(1.5, 0.5, 1, 2, 1, 1.5))
    extends Character {}

object Character {

  def apply(role: String, characterName: String, statistics: Statistics): Character =
    role match {
      case "Warrior" => Warrior(characterName, statistics)
      case "Thief"   => Thief(characterName, statistics)
      case "Wizard"  => Wizard(characterName, statistics)
      case "Healer"  => Healer(characterName, statistics)
    }

}
