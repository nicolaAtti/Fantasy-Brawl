package model

import MoveType._

sealed trait Move {
  def baseDamage: Int
  def moveType: MoveType
}

case class PhysicalAttack(baseDamage: Int) extends Move {
  override val moveType = Melee
}

case class SpecialMove(name: String,
                       manaCost: Int,
                       baseDamage: Int,
                       moveType: MoveType,
                       causesAfflictions: Set[Affliction],
                       causesModifiers: Set[Modifier],
                       nTargets: Int)
    extends Move

object Move {

//  def canMakeMove(whoWantsToMakeIt: Character, move: Move): Boolean = whoWantsToMakeIt hasEnoughManaPoints move

  def makeMove(move: Move, whoMakesIt: Character, Targets: List[Character]): MoveEffect = ???

  private def hasEnoughManaPoints(character: Character, move: Move): Boolean = move match {
    case PhysicalAttack(_)                       => true
    case SpecialMove(_, manaCost, _, _, _, _, _) => manaCost <= character.status.manaPoints
    case _                                       => false
  }

  import Affliction._
//  private def incapacitate(afflictions: Set[Affliction], move: Move): Boolean =
//    afflictions.contains(Stunned) || afflictions.contains(Asleep) ||
//      (move match {
//        case PhysicalAttack(_)                       => true
//        case SpecialMove(_, _, _, moveType, _, _, _) => if (afflictions.contains(Berserk)) false else moveType match {
//          case Melee => !afflictions.contains(Frozen)
//          case Ranged => !afflictions.contains(Blinded)
//          case Spell => !afflictions.contains(Silenced)
//
//        }
//      })

}
