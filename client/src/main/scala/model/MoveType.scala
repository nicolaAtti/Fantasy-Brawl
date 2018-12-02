package model

sealed trait MoveType {
  def attackingBonus(character: Character): Int
  def attackingCriticalBonus(character: Character): Int
  def defendingBonus(character: Character): Int
}

object MoveType {

  case object Melee extends MoveType {
    override def attackingBonus(character: Character): Int = character.physicalDamage
    override def attackingCriticalBonus(character: Character): Int = character.physicalCriticalDamage
    override def defendingBonus(character: Character): Int = character.physicalDefence
  }

  case object Ranged extends MoveType {
    override def attackingBonus(character: Character): Int = ???
    override def attackingCriticalBonus(character: Character): Int = ???
    override def defendingBonus(character: Character): Int = ???
  }

  case object Spell extends MoveType {
    override def attackingBonus(character: Character): Int = character.magicalDamage
    override def attackingCriticalBonus(character: Character): Int = character.magicalCriticalDamage
    override def defendingBonus(character: Character): Int = character.magicalDefence
  }

}
