package model

import MoveType._
import MoveEffects._
import model.Alteration._

sealed trait Move {
  def moveType: MoveType
  def moveEffect: (Character, Character) => Status
  def manaCost: Int
  def maxTargets: Int
}

case object PhysicalAttack extends Move {
  override val moveType = Melee
  override val moveEffect = standardDamageEffect(
    moveType = this.moveType,
    baseDamage = 0,
    addModifiers = Map.empty[String, Modifier],
    addAlterations = Map.empty[Alteration, Int],
    removeAlterations = Set[Alteration](Asleep)
  )
  override val manaCost = 0
  override val maxTargets = 1
}

case class SpecialMove(override val moveType: MoveType,
                       override val moveEffect: (Character, Character) => Status,
                       manaCost: Int,
                       maxTargets: Int)
    extends Move

object Move {

  def canMakeMove(c: Character, m: Move): Boolean = hasEnoughMana(c, m) && !isIncapacitated(c, m)

  private def hasEnoughMana(character: Character, move: Move): Boolean = character.status.manaPoints >= move.manaCost

  private def isIncapacitated(character: Character, move: Move): Boolean =
    character.status.alterations
      .map { case (alteration, _) => alteration.inhibits(move) }
      .fold(false)(_ || _)

  def makeMove(move: Move, attacker: Character, targets: Set[Character]): Map[Character, Status] = {
    require(canMakeMove(attacker, move) && targets.size <= move.maxTargets)

    val results: Map[Character, Status] =
      targets
        .map(target => (target, move.moveEffect(attacker, target)))
        .toMap

    import Status._
    if (results contains attacker)
      adjust(results, attacker)(afterManaConsumption(_, move))
    else
      results + (attacker -> afterManaConsumption(attacker.status, move))
  }

  private def adjust[A, B](m: Map[A, B], k: A)(f: B => B): Map[A, B] = m.updated(k, f(m(k)))

  // Move Factory

  implicit def strToMoveType(str: String): MoveType = MoveType(str)

  implicit def listStrToAlterationsMap(listStr: List[String]): Map[Alteration, Int] =
    listStr
      .map(a => Alteration(a))
      .map(a => (a, a.turnDuration))
      .toMap

  implicit def listTupleToModifiersMap(listTuple: List[(String, String, Int, Int)]): Map[String, Modifier] =
    listTuple.map {
      case (modifierName, subStatisticName, delta, duration) =>
        (modifierName, Modifier(SubStatistic(subStatisticName), delta, duration))
    }.toMap

//  def apply(damageType: String, moveType: MoveType, ): Move = damageType match {
//    case "PhysicalAttack" => PhysicalAttack
//    case ""
//  }
}
