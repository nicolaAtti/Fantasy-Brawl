package model

/** Every move is associated to a move type that has a twofold purpose:
  *
  * 1) It determines which attacker's and target's sub-statistics could affect the
  * move's power.
  *
  * For example, the damage effect of a Melee move will be higher if the attacker
  * has a high physicalDamage sub-statistic, whilst the damage effect of a Spell
  * will be higher if the attacker has a high magical power sub-statistic.
  *
  * 2) Different status alterations inhibit different move types.
  *
  * For example, a silenced character cannot cast any Spell, whilst a blinded
  * character cannot make any Ranged move.
  */
sealed trait MoveType {

  /** Provides the attacking bonus given the attacker character.
    *
    * @param attacker the character making the move
    * @return the attacking bonus
    */
  def attackingBonus(attacker: Character): Int

  /** Provides the attacking critical bonus given the attacker character.
    *
    * @param attacker the character making the move
    * @return the attacking critical bonus
    */
  def attackingCriticalBonus(attacker: Character): Int

  /** Provides the defending bonus given the target character.
    *
    * @param target the target character
    * @return the defending bonus
    */
  def defendingBonus(target: Character): Int

  /** A string representation of the move type. */
  def representation: String
}

object MoveType {

  /** Defines a move of type melee.
    *
    * The power of a melee move is influenced by the attacker's physical attack
    * bonus and the target's physical defence. When critical, the bonus damage
    * is influenced by the attacker's physical critical bonus.
    */
  case object Melee extends MoveType {
    override def attackingBonus(attacker: Character): Int = attacker.physicalDamage
    override def attackingCriticalBonus(attacker: Character): Int = attacker.physicalCriticalDamage
    override def defendingBonus(target: Character): Int = target.physicalDefence
    override val representation: String = "Melee"
  }
  /** Defines a move of type ranged.
    *
    * The power of a ranged move is influenced by the attacker's physical attack
    * bonus and the target's physical defence. When critical, the bonus damage
    * is influenced by the attacker's physical critical bonus.
    */
  case object Ranged extends MoveType {
    override def attackingBonus(attacker: Character): Int = attacker.physicalDamage
    override def attackingCriticalBonus(attacker: Character): Int = attacker.physicalCriticalDamage
    override def defendingBonus(target: Character): Int = target.physicalDefence
    override val representation: String = "Ranged"
  }

  /** Defines a move of type spell.
    *
    * The power of a spell is influenced by the attacker's magical power and the
    * target's magical defence. When critical, the bonus damage/healing caused by
    * the spell is influenced by the attacker's magical critical power.
    */
  case object Spell extends MoveType {
    override def attackingBonus(attacker: Character): Int = attacker.magicalPower
    override def attackingCriticalBonus(attacker: Character): Int = attacker.magicalCriticalPower
    override def defendingBonus(target: Character): Int = target.magicalDefence
    override val representation: String = "Spell"
  }

  /** Retrieves the appropriate type of move given its string representation.
    *
    * @param representation the move type string representation
    * @return the corresponding move type object
    */
  def apply(representation: String): MoveType = representation match {
    case Melee.representation  => Melee
    case Ranged.representation => Ranged
    case Spell.representation  => Spell
    case _                     => throw new IllegalArgumentException(s"Unknown move type: $representation")
  }

}
