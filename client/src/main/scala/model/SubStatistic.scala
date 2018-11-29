package model

trait SubStatistic

object SubStatistic {
  case object PhysicalDamage extends SubStatistic
  case object MagicalDamage extends SubStatistic
  case object PhysicalCriticalDamage extends SubStatistic
  case object MagicalCriticalDamage extends SubStatistic
  case object PhysicalDefence extends SubStatistic
  case object MagicalDefence extends SubStatistic
  case object CriticalChance extends SubStatistic
  case object Speed extends SubStatistic
}

