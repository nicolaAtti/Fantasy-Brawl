package model

import scala.util.Random

object MoveEffects {

  val minDamage = 1

  import model.Move._
  import MoveEffectHelper._

  def standardDamageEffect(moveType: MoveType,
                           baseDamage: Int,
                           addModifiers: Map[String, Modifier],
                           addAlterations: Map[Alteration, Int],
                           removeAlterations: Set[Alteration])(attacker: Attacker, target: Target): NewTargetStatus = {
    val normalDamage = baseDamage + moveType.attackingBonus(attacker) - moveType.defendingBonus(target)
    val actualDamage = minDamage max afterCriticalEvaluation(normalDamage,
                                                             criticalChance = attacker.criticalChance,
                                                             criticalBonus = moveType.attackingCriticalBonus(attacker))
    updatedStatus(
      originalStatus = target.status,
      healthPointsToClamp = target.status.healthPoints - actualDamage,
      modifiersToAdd = addModifiers,
      alterationsToAdd = addAlterations,
      alterationsToRemove = removeAlterations
    )
  }

  def standardHealEffect(baseHeal: Int,
                         addModifiers: Map[String, Modifier],
                         addAlterations: Map[Alteration, Int],
                         removeAlterations: Set[Alteration])(attacker: Attacker, target: Target): NewTargetStatus = {
    val normalHeal = baseHeal + attacker.magicalPower
    val actualHeal = afterCriticalEvaluation(normalHeal,
                                             criticalChance = attacker.criticalChance,
                                             criticalBonus = attacker.magicalCriticalPower)
    updatedStatus(
      originalStatus = target.status,
      healthPointsToClamp = target.status.healthPoints + actualHeal,
      modifiersToAdd = addModifiers,
      alterationsToAdd = addAlterations,
      alterationsToRemove = removeAlterations
    )
  }

  def percentageEffect(percentage: Int,
                       addModifiers: Map[String, Modifier],
                       addAlterations: Map[Alteration, Int],
                       removeAlterations: Set[Alteration])(attacker: Attacker, target: Target): NewTargetStatus = {
    updatedStatus(
      originalStatus = target.status,
      healthPointsToClamp = target.status.healthPoints * percentage / 100,
      modifiersToAdd = addModifiers,
      alterationsToAdd = addAlterations,
      alterationsToRemove = removeAlterations
    )
  }

  def buffDebuffEffect(addModifiers: Map[String, Modifier],
                       addAlterations: Map[Alteration, Int],
                       removeAlterations: Set[Alteration])(attacker: Attacker, target: Target): NewTargetStatus = {
    updatedStatus(
      originalStatus = target.status,
      healthPointsToClamp = target.status.healthPoints,
      modifiersToAdd = addModifiers,
      alterationsToAdd = addAlterations,
      alterationsToRemove = removeAlterations
    )
  }
}

private object MoveEffectHelper {

  def afterCriticalEvaluation(value: Int, criticalChance: Int, criticalBonus: Double): Int = {
    val criticalValue = value * criticalBonus / 100
    val isCritical = criticalChance > Random.nextInt(100)
    if (isCritical) Math.round(criticalValue).toInt else value
  }

  def updatedStatus(originalStatus: Status,
                    healthPointsToClamp: Int,
                    modifiersToAdd: Map[String, Modifier],
                    alterationsToAdd: Map[Alteration, Int],
                    alterationsToRemove: Set[Alteration]): Status = {
    originalStatus.copy(
      healthPoints = clamped(healthPointsToClamp, minValue = 0, maxValue = originalStatus.maxHealthPoints),
      modifiers = originalStatus.modifiers ++ modifiersToAdd,
      alterations = originalStatus.alterations ++ alterationsToAdd -- alterationsToRemove
    )
  }

  def clamped(value: Int, minValue: Int, maxValue: Int): Int = minValue max value min maxValue

}
