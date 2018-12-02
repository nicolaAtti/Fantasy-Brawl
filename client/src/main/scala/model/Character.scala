package model

import utilities.Utility._

trait Character {
  val characterName: String
  val statistics: Statistics
  val classMultipliers: ClassMultipliers

  /**
    * Contains the character's status, depending on current HP and MP values and existing modifiers and afflictions
    */
  var status: Status = Status(
    healthPoints = calculateMaxHealthPoints(),
    manaPoints = calculateMaxManaPoints(),
    maxHealthPoints = calculateMaxHealthPoints(),
    maxManaPoints = calculateMaxManaPoints(),
    Map(),
    Map()
  )

  private def calculateMaxHealthPoints(): Int = {
    roundDown((this.statistics.resistance * this.classMultipliers.healthPoints) * 10)
  }

  private def calculateMaxManaPoints(): Int = {
    roundDown((this.statistics.spirit * this.classMultipliers.spirit) * 5)
  }

  import SubStatistic._

  /**
    * Calculates the character's physical damage
    * @return
    */
  def physicalDamage: Int =
    roundDown(statistics.strength * classMultipliers.strength) + getModifierValues(PhysicalDamage)

  /**
    * Calculates the character's bonus physical critical strike damage
    * @return
    */
  def physicalCriticalDamage: Int =
    roundDown((statistics.strength * classMultipliers.strength) / 5 + 150) + getModifierValues(PhysicalCriticalDamage)

  /**
    * Calculates the character's speed
    * @return
    */
  def speed: Int =
    roundDown((statistics.agility * classMultipliers.agility) / 10) + getModifierValues(Speed)

  /**
    * Calculates the character's critical strike chance
    * @return
    */
  def criticalChance: Int =
    roundDown((statistics.agility * classMultipliers.agility) / 2) + getModifierValues(CriticalChance)

  /**
    * Calculates the character's magical defence
    * @return
    */
  def magicalDefence: Int =
    roundDown(statistics.spirit * classMultipliers.spirit) + getModifierValues(MagicalDefence)

  /**
    * Calculates the character's magical damage
    * @return
    */
  def magicalDamage: Int =
    roundDown(statistics.intelligence * classMultipliers.intelligence) + getModifierValues(MagicalDamage)

  /**
    * Calculates the character's bonus magical critical strike damage
    * @return
    */
  def magicalCriticalDamage: Int =
    roundDown((statistics.intelligence * classMultipliers.intelligence) / 10 + 150) + getModifierValues(
      MagicalCriticalDamage)

  /**
    * Calculates the character's physical defence
    * @return
    */
  def physicalDefence: Int =
    roundDown(statistics.resistance * classMultipliers.resistance) + getModifierValues(PhysicalDefence)

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    * @param subStatistic the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    * @author Nicola Atti
    */
  private def getModifierValues(subStatistic: SubStatistic): Int = {
//    var allModsValue = 0
//    status.modifiers foreach (modifier => allModsValue = add(allModsValue, modifier._2.value))
//    allModsValue
    status.modifiers
      .filter(kv => kv._2.affectsSubStatistic == subStatistic)
      .map(kv => kv._2.delta)
      .sum
  }

}

private case class Warrior(characterName: String,
                           statistics: Statistics,
                           classMultipliers: ClassMultipliers = ClassMultipliers(2, 1, 1, 0.5, 1.5, 2))
    extends Character {}

private case class Thief(characterName: String,
                         statistics: Statistics,
                         classMultipliers: ClassMultipliers = ClassMultipliers(1.5, 2, 1, 0.5, 1, 1.5))
    extends Character {}

private case class Wizard(characterName: String,
                          statistics: Statistics,
                          classMultipliers: ClassMultipliers = ClassMultipliers(1, 1, 1.5, 2, 0.5, 1.5))
    extends Character {}

private case class Healer(characterName: String,
                          statistics: Statistics,
                          classMultipliers: ClassMultipliers = ClassMultipliers(1.5, 0.5, 1, 2, 1, 1.5))
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
