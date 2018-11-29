package model

import utilities.Utility._

trait Character {
  val characterName: String
  val statistics: Statistics
  val classMultipliers: ClassStat

  /**
    * Contains the character's status, depending on current HP and MP values and existing modifiers and afflictions
    */
  var status: ImmutableStatus = ImmutableStatus(
    healthPoints = calculateMaxHealthPoints(),
    manaPoints = calculateMaxManaPoints(),
    maxHealthPoints = calculateMaxHealthPoints(),
    maxManaPoints = calculateMaxManaPoints(),
    Map(),
    Map()
  )

  private def calculateMaxHealthPoints(): Int = {
    roundDown((this.statistics.resistance * this.classMultipliers.hpMod) * 10)
  }

  private def calculateMaxManaPoints(): Int = {
    roundDown((this.statistics.spirit * this.classMultipliers.spiritMod) * 5)
  }
  

  /**
    * Calculates the character's physical damage
    * @return
    */
  def physicalDamage: Int =
    roundDown(statistics.strength * classMultipliers.strengthMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus physical critical strike damage
    * @return
    */
  def pysicalCriticalDamage: Int =
    roundDown((statistics.strength * classMultipliers.strengthMod) / 5 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's speed
    * @return
    */
  def speed: Int =
    roundDown((statistics.agility * classMultipliers.agilityMod) / 10) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's critical strike chance
    * @return
    */
  def criticalChance: Int =
    roundDown((statistics.agility * classMultipliers.agilityMod) / 2) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical defence
    * @return
    */
  def magicalDefence: Int =
    roundDown(statistics.spirit * classMultipliers.spiritMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical damage
    * @return
    */
  def magicalDamage: Int =
    roundDown(statistics.intelligence * classMultipliers.intelligenceMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus magical critical strike damage
    * @return
    */
  def magicalCriticalDamage: Int =
    roundDown((statistics.intelligence * classMultipliers.intelligenceMod) / 10 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's physical defence
    * @return
    */
  def physicalDefence: Int =
    roundDown(statistics.resistance * classMultipliers.resistanceMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    * @param affectedStat the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    * @author Nicola Atti
    */
  private def getModifierValues(affectedStat: String): Int = {
    var allModsValue = 0
    status.modifiers foreach (modifier => allModsValue = add(allModsValue, modifier._2.value))
    allModsValue
  }

}

private case class Warrior(characterName: String,
                           statistics: Statistics,
                           classMultipliers: ClassStat = ClassStat(2, 1, 1, 0.5, 1.5, 2))
    extends Character {}

private case class Thief(characterName: String,
                         statistics: Statistics,
                         classMultipliers: ClassStat = ClassStat(1.5, 2, 1, 0.5, 1, 1.5))
    extends Character {}

private case class Wizard(characterName: String,
                          statistics: Statistics,
                          classMultipliers: ClassStat = ClassStat(1, 1, 1.5, 2, 0.5, 1.5))
    extends Character {}

private case class Healer(characterName: String,
                          statistics: Statistics,
                          classMultipliers: ClassStat = ClassStat(1.5, 0.5, 1, 2, 1, 1.5))
    extends Character {}

object Character {

  def apply(role: String, charName: String, stats: Statistics): Character =
    role match {
      case "Warrior" => Warrior(charName, stats)
      case "Thief"   => Thief(charName, stats)
      case "Wizard"  => Wizard(charName, stats)
      case "Healer"  => Healer(charName, stats)
    }

}
