package model

import scala.util.Random
import MoveType._

sealed trait Move {
  def moveType: MoveType

  type TargetStatus = Status
  def moveEffect(attacker: Character, target: Character): TargetStatus
}

case object PhysicalAttack extends Move {
  override val moveType = Melee

  override def moveEffect(attacker: Character, target: Character): Status = {
    val normalDamage: Int = this.moveType.attackingBonus(attacker) - this.moveType.defendingBonus(target)
    val criticalDamage: Double = normalDamage * this.moveType.attackingCriticalBonus(attacker) / 100
    val isCritical: Boolean = attacker.criticalChance > Random.nextInt(100)

    val damage: Int = if (isCritical) Math.round(criticalDamage).toInt else normalDamage
    val clampedDamage: Int = 1 max damage min target.status.healthPoints // at least 1 damage, not more than target's hp

    target.status.copy(healthPoints = target.status.healthPoints - clampedDamage)
  }
}

case class SpecialMove(moveType: MoveType,
                       manaCost: Int,
                       maxTargets: Int,
                       _moveEffect: (Character, Character) => Status)
    extends Move {
  override def moveEffect(attacker: Character, target: Character): TargetStatus = _moveEffect(attacker, target)
}

object Move {

  def canMakeMove(c: Character, m: Move): Boolean = hasEnoughMana(c, m) && !isIncapacitated(c, m)

  private def hasEnoughMana(character: Character, move: Move): Boolean = move match {
    case PhysicalAttack => true
    case SpecialMove(_, manaCost, _, _) => character.status.manaPoints >= manaCost
  }

  private def isIncapacitated(character: Character, move: Move): Boolean =
    character.status.afflictions
      .map(kv => kv._1.inhibits(move))
      .fold(false)(_ || _)

  def makeMove(move: Move, attacker: Character, targets: Set[Character]): Map[Character, Status] = ???

}
