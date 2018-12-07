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

  implicit def listStrToAlterationsSet(listStr: List[String]): Set[Alteration] =
    listStr
      .map(a => Alteration(a))
      .toSet

  implicit def listTupleToModifiersMap(listTuple: List[(String, String, Int, Int)]): Map[String, Modifier] =
    listTuple.map {
      case (modifierName, subStatisticName, delta, duration) =>
        (modifierName, Modifier(SubStatistic(subStatisticName), delta, duration))
    }.toMap

  def apply(damageType: String,
            moveType: MoveType,
            baseValue: Int,
            addModifiers: Map[String, Modifier],
            addAlterations: Map[Alteration, Int],
            removeAlterations: Set[Alteration],
            manaCost: Int,
            maxTargets: Int): Move = {

    require(manaCost >= 0, "The mana cost cannot be negative")
    require(baseValue >= 0, "The base value cannot be negative")
    require(maxTargets > 0, "The number of maximum targets must be at least one")

    damageType match {

      case "PhysicalAttack" => PhysicalAttack

      case "StandardDamage" =>
        val moveEffect: (Character, Character) => Status =
          standardDamageEffect(moveType = moveType,
                               baseDamage = baseValue,
                               addModifiers = addModifiers,
                               addAlterations = addAlterations,
                               removeAlterations = removeAlterations)
        SpecialMove(moveType, moveEffect, manaCost, maxTargets)

      case "StandardHeal" =>
        val moveEffect: (Character, Character) => Status =
          standardHealEffect(baseHeal = baseValue,
                             addModifiers = addModifiers,
                             addAlterations = addAlterations,
                             removeAlterations = removeAlterations)
        SpecialMove(moveType, moveEffect, manaCost, maxTargets)

      case "Percentage" =>
        val moveEffect: (Character, Character) => Status =
          percentageEffect(percentage = baseValue,
                           addModifiers = addModifiers,
                           addAlterations = addAlterations,
                           removeAlterations = removeAlterations)
        SpecialMove(moveType, moveEffect, manaCost, maxTargets)

      case "BuffDebuff" =>
        val moveEffect: (Character, Character) => Status =
          buffDebuffEffect(addModifiers = addModifiers,
                           addAlterations = addAlterations,
                           removeAlterations = removeAlterations)
        SpecialMove(moveType, moveEffect, manaCost, maxTargets)

      case _ => throw new IllegalArgumentException(s"Unknown move damage type: $damageType")

    }
  }

}
