package model

import utilities.Utility._

trait Character {
  val characterName: String
  val role: String
  val statistics: Statistics
  val classMultipliers: ClassMultipliers
  val specialMoves: Map[String, SpecialMove]

  /**
    * Contains the character's status, depending on current HP and MP values and current modifiers and afflictions
    *
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
    *
    * @return the character's physical damage
    */
  def physicalDamage: Int =
    roundDown(statistics.strength * classMultipliers.strength) + getModifierValues(PhysicalDamage)

  /**
    * Calculates the character's bonus physical critical strike damage
    *
    * @return the character's physical critical strike damage
    */
  def physicalCriticalDamage: Int =
    roundDown((statistics.strength * classMultipliers.strength) / 5 + 150) + getModifierValues(PhysicalCriticalDamage)

  /**
    * Calculates the character's speed
    *
    * @return the character's speed
    */
  def speed: Int =
    roundUp((statistics.agility * classMultipliers.agility) / 10) + getModifierValues(Speed)

  /**
    * Calculates the character's critical strike chance
    *
    * @return the character's critical strike chance
    */
  def criticalChance: Int =
    roundDown((statistics.agility * classMultipliers.agility) / 2) + getModifierValues(CriticalChance)

  /**
    * Calculates the character's magical defence
    *
    * @return the character's magical defence
    */
  def magicalDefence: Int =
    roundDown(statistics.spirit * classMultipliers.spirit) + getModifierValues(MagicalDefence)

  /**
    * Calculates the character's magical power
    *
    * @return the character's magical power
    */
  def magicalPower: Int =
    roundDown(statistics.intelligence * classMultipliers.intelligence) + getModifierValues(MagicalDamage)

  /**
    * Calculates the character's bonus magical critical strike power
    *
    * @return the character's magical critical strike power
    */
  def magicalCriticalPower: Int =
    roundDown((statistics.intelligence * classMultipliers.intelligence) / 10 + 150) + getModifierValues(
      MagicalCriticalDamage)

  /**
    * Calculates the character's physical defence
    *
    * @return the character's physical defence
    */
  def physicalDefence: Int =
    roundDown(statistics.resistance * classMultipliers.resistance) + getModifierValues(PhysicalDefence)

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    *
    * @param subStatistic the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    *
    */
  private def getModifierValues(subStatistic: SubStatistic): Int = {
    status.modifiers
      .filter { case (_, modifier) => modifier.affectsSubStatistic == subStatistic }
      .map { case (_, modifier) => modifier.delta }
      .sum
  }

}

private case class Warrior(characterName: String,
                           statistics: Statistics,
                           specialMoves: Map[String, SpecialMove],
                           classMultipliers: ClassMultipliers = ClassMultipliers(2, 1, 1, 0.5, 1.5, 2),
                           role: String = "Warrior"
                          )
    extends Character {}

private case class Thief(characterName: String,
                         statistics: Statistics,
                         specialMoves: Map[String, SpecialMove],
                         classMultipliers: ClassMultipliers = ClassMultipliers(1.5, 2, 1, 0.5, 1, 1.5),
                         role: String = "Thief")
    extends Character {}

private case class Wizard(characterName: String,
                          statistics: Statistics,
                          specialMoves: Map[String, SpecialMove],
                          classMultipliers: ClassMultipliers = ClassMultipliers(1, 1, 1.5, 2, 0.5, 1.5),
                          role: String = "Wizard")
    extends Character {}

private case class Healer(characterName: String,
                          statistics: Statistics,
                          specialMoves: Map[String, SpecialMove],
                          classMultipliers: ClassMultipliers = ClassMultipliers(1.5, 0.5, 1, 2, 1, 1.5),
                          role: String = "Healer")
    extends Character {}

object Character {

  def apply(characterClass: String,
            characterName: String,
            statistics: Statistics,
            specialMoves: Map[String, SpecialMove]): Character =
    characterClass match {
      case "Warrior" => Warrior(characterName, statistics, specialMoves)
      case "Thief"   => Thief(characterName, statistics, specialMoves)
      case "Wizard"  => Wizard(characterName, statistics, specialMoves)
      case "Healer"  => Healer(characterName, statistics, specialMoves)
      case _         => throw new IllegalArgumentException(s"Unknown character role: $characterClass")
    }

}
