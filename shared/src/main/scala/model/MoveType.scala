package model

sealed trait MoveType {
  def attackingBonus(character: Character): Int
  def attackingCriticalBonus(character: Character): Int
  def defendingBonus(character: Character): Int
  def representation: String
}

object MoveType {

  def apply(name: String): MoveType = name match {
    case Melee.representation  => Melee
    case Ranged.representation => Ranged
    case Spell.representation  => Spell
    case _                     => throw new IllegalArgumentException(s"Unknown move type: $name")
  }

  case object Melee extends MoveType {
    override def attackingBonus(character: Character): Int = character.physicalDamage
    override def attackingCriticalBonus(character: Character): Int = character.physicalCriticalDamage
    override def defendingBonus(character: Character): Int = character.physicalDefence
    override val representation: String = "Melee"
  }

  case object Ranged extends MoveType {
    override def attackingBonus(character: Character): Int = character.physicalDamage
    override def attackingCriticalBonus(character: Character): Int = character.physicalCriticalDamage
    override def defendingBonus(character: Character): Int = character.physicalDefence
    override val representation: String = "Ranged"
  }

  case object Spell extends MoveType {
    override def attackingBonus(character: Character): Int = character.magicalPower
    override def attackingCriticalBonus(character: Character): Int = character.magicalCriticalPower
    override def defendingBonus(character: Character): Int = character.magicalDefence
    override val representation: String = "Spell"
  }

}
