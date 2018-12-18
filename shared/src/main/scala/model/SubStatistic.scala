package model

trait SubStatistic

/** A module that contains all the possible sub-statistics of a character.
  *
  * The sub-statistics influence the effectiveness of a character in combat.
  *
  * @author Nicola Atti
  */
object SubStatistic {

  /** Represents the bonus physical damage of a character */
  case object PhysicalDamage extends SubStatistic

  /** Represents the bonus magical damage of a character */
  case object MagicalDamage extends SubStatistic

  /** Represents the bonus physical critical damage of a character */
  case object PhysicalCriticalDamage extends SubStatistic

  /** Represents the bonus magical critical damage of a character */
  case object MagicalCriticalDamage extends SubStatistic

  /** Represents the physical defence of a character */
  case object PhysicalDefence extends SubStatistic

  /** Represents the magical defence of a character */
  case object MagicalDefence extends SubStatistic

  /** Represents the critical strike chance of a character */
  case object CriticalChance extends SubStatistic

  /** Represents the speed of a character */
  case object Speed extends SubStatistic

  /** Retrieves the appropriate sub-statistic object given its name.
    *
    * @param name the sub-statistic's name
    * @return the corresponding object
    */
  def apply(name: String): SubStatistic = name match {
    case "PhysicalDamage"         => PhysicalDamage
    case "MagicalDamage"          => MagicalDamage
    case "PhysicalCriticalDamage" => PhysicalCriticalDamage
    case "MagicalCriticalDamage"  => MagicalCriticalDamage
    case "PhysicalDefence"        => PhysicalDefence
    case "MagicalDefence"         => MagicalDefence
    case "CriticalChance"         => CriticalChance
    case "Speed"                  => Speed
    case _                        => throw new IllegalArgumentException(s"Unknown sub-statistic name: $name")
  }

}
