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
