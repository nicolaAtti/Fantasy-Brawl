package model

import MoveType._

sealed trait Move {
  def moveType: MoveType
  def statusAlteration: StatusAlteration
}

case class PhysicalAttack(moveType: MoveType, statusAlteration: StatusAlteration) extends Move

object PhysicalAttack {

  def apply(baseDamage: Int): PhysicalAttack =
    new PhysicalAttack(
      moveType = Melee,
      statusAlteration = StatusAlteration(healthPoints = (hp: Int) => hp - baseDamage,
                                          manaPoints = (mp: Int) => mp,
                                          newModifiers = Map(),
                                          newAfflictions = Map())
    )
}

case class SpecialMove(name: String,
                       moveType: MoveType,
                       manaCost: Int,
                       statusAlteration: StatusAlteration,
                       nTargets: Int)
    extends Move

object Move {

  def canMakeMove(c: Character, m: Move): Boolean = hasEnoughMana(c, m) && !isIncapacitated(c, m)

  private def hasEnoughMana(character: Character, move: Move): Boolean = move match {
    case PhysicalAttack(_, _)              => true
    case SpecialMove(_, _, manaCost, _, _) => character.status.manaPoints >= manaCost
    case _                                 => false
  }

  private def isIncapacitated(character: Character, move: Move): Boolean =
    character.status.afflictions
      .map(kv => kv._1.inhibits(move))
      .fold(false)(_ || _)

  def makeMove(move: Move, whoMakesIt: Character, Targets: List[Character]): StatusAlteration = ???

}
