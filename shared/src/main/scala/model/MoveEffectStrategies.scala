package model

import scala.util.Random

object MoveEffectStrategies {

  val minDamage = 1

  import model.Move._
  import MoveEffectStrategiesHelper._

  def createStandardDamageEffect(moveType: MoveType,
                                 baseDamage: Int,
                                 addModifiers: Map[String, Modifier],
                                 addAlterations: Map[Alteration, Int],
                                 removeAlterations: Set[Alteration]) =
    (attacker: Attacker, target: Target) => {
      val normalDamage = baseDamage + moveType.attackingBonus(attacker) - moveType.defendingBonus(target)
      val actualDamage = minDamage max afterCriticalEvaluation(
        normalDamage,
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

  def createStandardHealEffect(baseHeal: Int,
                               addModifiers: Map[String, Modifier],
                               addAlterations: Map[Alteration, Int],
                               removeAlterations: Set[Alteration]) =
    (attacker: Attacker, target: Target) => {
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

  def createPercentageEffect(percentage: Int,
                             addModifiers: Map[String, Modifier],
                             addAlterations: Map[Alteration, Int],
                             removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) => {

      updatedStatus(
        originalStatus = target.status,
        healthPointsToClamp = target.status.healthPoints * percentage / 100,
        modifiersToAdd = addModifiers,
        alterationsToAdd = addAlterations,
        alterationsToRemove = removeAlterations
      )
    }

  def createBuffDebuffEffect(addModifiers: Map[String, Modifier],
                             addAlterations: Map[Alteration, Int],
                             removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) => {

      updatedStatus(
        originalStatus = target.status,
        healthPointsToClamp = target.status.healthPoints,
        modifiersToAdd = addModifiers,
        alterationsToAdd = addAlterations,
        alterationsToRemove = removeAlterations
      )
    }

  private object MoveEffectStrategiesHelper {

    def afterCriticalEvaluation(value: Int, criticalChance: Int, criticalBonus: Double): Int = {
      val criticalValue = value * criticalBonus / 100
      val isCritical = criticalChance > Random.nextInt(100)
      if (isCritical) Math.round(criticalValue).toInt else value
    }

    import utilities.Misc.clamped

    /**
      *
      * @param originalStatus
      * @param healthPointsToClamp
      * @param modifiersToAdd
      * @param alterationsToAdd
      * @param alterationsToRemove
      * @return
      */
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

  }

}
