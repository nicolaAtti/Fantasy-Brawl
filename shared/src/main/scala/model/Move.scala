package model

import Move._
import MoveType._
import MoveEffectStrategies._
import model.Alteration._

sealed trait Move {
  def moveType: MoveType
  def moveEffect: (Attacker, Target) => NewTargetStatus
  def manaCost: Int
  def maxTargets: Int
}

object Move {
  import MoveHelper._

  type Attacker = Character
  type Target = Character
  type NewTargetStatus = Status
  type NewStatuses = Map[Character, Status]

  def canMakeMove(attacker: Attacker, move: Move): Boolean =
    hasEnoughMana(attacker, move) && !isInhibited(attacker, move)

  def makeMove(move: Move, attacker: Attacker, targets: Set[Target]): NewStatuses = {
    require(canMakeMove(attacker, move) && targets.size <= move.maxTargets)

    val results: NewStatuses =
      targets
        .map(target => (target, move.moveEffect(attacker, target)))
        .toMap

    import Status._
    if (results contains attacker)
      adjust(map = results, key = attacker)(afterManaConsumption(_, move))
    else
      results + (attacker -> afterManaConsumption(attacker.status, move))
  }

  private object MoveHelper {

    def hasEnoughMana(character: Attacker, move: Move): Boolean = character.status.manaPoints >= move.manaCost

    def isInhibited(character: Attacker, move: Move): Boolean =
      character.status.alterations
        .map { case (alteration, _) => alteration.inhibits(move) }
        .fold(false)(_ || _)

    def adjust[A, B](map: Map[A, B], key: A)(f: B => B): Map[A, B] = map.updated(key, f(map(key)))

  }

}

case object PhysicalAttack extends Move {
  override val moveType: MoveType = Melee
  override val moveEffect: (Attacker, Target) => NewTargetStatus =
    createStandardDamageEffect(moveType = this.moveType, baseDamage = 0)(addModifiers = Map.empty,
                                                                         addAlterations = Map.empty,
                                                                         removeAlterations = Set(Asleep))
  override val manaCost = 0
  override val maxTargets = 1
}

case class SpecialMove(name: String,
                       moveType: MoveType,
                       moveEffect: (Attacker, Target) => NewTargetStatus,
                       manaCost: Int,
                       maxTargets: Int)
    extends Move {
  override def toString: String =
    s"[$name] Move type: ${moveType.representation} | Mana cost: $manaCost | Max targets: $maxTargets"
}

object SpecialMove {

  def apply(name: String,
            moveEffectStrategyCode: String,
            moveType: MoveType,
            baseValue: Int,
            addModifiers: Map[String, Modifier],
            addAlterations: Map[Alteration, Int],
            removeAlterations: Set[Alteration],
            manaCost: Int,
            maxTargets: Int): SpecialMove = {

    require(manaCost >= 0, "The mana cost cannot be negative")
    require(baseValue >= 0, "The base value cannot be negative")
    require(maxTargets > 0, "The number of maximum targets must be at least one")

    implicit val modifiersToAdd: Map[String, Modifier] = addModifiers
    implicit val alterationsToAdd: Map[Alteration, Int] = addAlterations
    implicit val alterationsToRemove: Set[Alteration] = removeAlterations

    val moveEffect = moveEffectStrategyCode match {
      case "StandardDamage" => createStandardDamageEffect(moveType = moveType, baseDamage = baseValue)
      case "StandardHeal"   => createStandardHealEffect(baseHeal = baseValue)
      case "Percentage"     => createPercentageEffect(percentage = baseValue)
      case "BuffDebuff"     => createBuffDebuffEffect
      case _                => throw new IllegalArgumentException(s"Unknown move effect strategy: $moveEffectStrategyCode")
    }

    SpecialMove(name, moveType, moveEffect, manaCost, maxTargets)
  }

}
