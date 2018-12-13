package model

trait SubStatistic

/** A module that contains all the possible sub-statistics of a character.
  *
  * The sub-statistics influence the effectiveness of a character in combat.
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

}
