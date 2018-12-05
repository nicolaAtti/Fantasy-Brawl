package model

import MoveType._
import MoveEffects._

sealed trait Move {
  def moveType: MoveType
  def moveEffect: (Character, Character) => Status
}

case object PhysicalAttack extends Move {
  override val moveType = Melee
  override val moveEffect = standardDamageEffect(
    moveType = this.moveType,
    baseDamage = 0,
    addModifiers = Map.empty[String, Modifier],
    addAlterations = Map.empty[Alteration, Int],
    removeAlterations = Set.empty[Alteration]
  )
}

case class SpecialMove(override val moveType: MoveType,
                       override val moveEffect: (Character, Character) => Status,
                       manaCost: Int,
                       maxTargets: Int)
    extends Move

object Move {

  def canMakeMove(c: Character, m: Move): Boolean = hasEnoughMana(c, m) && !isIncapacitated(c, m)

  private def hasEnoughMana(character: Character, move: Move): Boolean = move match {
    case PhysicalAttack                 => true
    case SpecialMove(_, _, manaCost, _) => character.status.manaPoints >= manaCost
  }

  private def isIncapacitated(character: Character, move: Move): Boolean =
    character.status.alterations
      .map { case (alteration, _) => alteration.inhibits(move) }
      .fold(false)(_ || _)

  def makeMove(move: Move, attacker: Character, targets: Set[Character]): Map[Character, Status] = ???

}
