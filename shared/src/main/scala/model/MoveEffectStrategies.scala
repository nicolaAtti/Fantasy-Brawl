package model

import scala.util.Random

object MoveEffectStrategies {

  val minDamage = 1

  import model.Move._
  import StrategiesHelper._

  def createStandardDamageEffect(moveType: MoveType, baseDamage: Int)(implicit addModifiers: Map[String, Modifier],
                                                                      addAlterations: Map[Alteration, Int],
                                                                      removeAlterations: Set[Alteration]) =
    (attacker: Attacker, target: Target) => {
      val normalDamage = baseDamage + moveType.attackingBonus(attacker) - moveType.defendingBonus(target)
      val actualDamage =
        minDamage max afterCriticalEvaluation(normalDamage,
                                              criticalChance = attacker.criticalChance,
                                              criticalBonus = moveType.attackingCriticalBonus(attacker))

      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints - actualDamage)
    }

  def createStandardHealEffect(baseHeal: Int)(implicit addModifiers: Map[String, Modifier],
                                              addAlterations: Map[Alteration, Int],
                                              removeAlterations: Set[Alteration]) =
    (attacker: Attacker, target: Target) => {
      val normalHeal = baseHeal + attacker.magicalPower
      val actualHeal =
        afterCriticalEvaluation(normalHeal,
                                criticalChance = attacker.criticalChance,
                                criticalBonus = attacker.magicalCriticalPower)

      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints + actualHeal)
    }

  def createPercentageEffect(percentage: Int)(implicit addModifiers: Map[String, Modifier],
                                              addAlterations: Map[Alteration, Int],
                                              removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) =>
      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints * percentage / 100)

  def createBuffDebuffEffect(implicit addModifiers: Map[String, Modifier],
                             addAlterations: Map[Alteration, Int],
                             removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) =>
      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints)

  private object StrategiesHelper {

    def afterCriticalEvaluation(value: Int, criticalChance: Int, criticalBonus: Double): Int = {
      val criticalValue = value * criticalBonus / 100
      val isCritical = criticalChance > Random.nextInt(100)
      if (isCritical) Math.round(criticalValue).toInt else value
    }

    import utilities.Misc.clamped

    /** Evaluates the new status after the move effect, given the original status,
      * the new health points (to validate) plus all the modifiers to add and the
      * alterations to add/remove to/from the original status.
      *
      * @param originalStatus the status before the move effect
      * @param healthPointsToClamp the new health points to clamp between zero and
      *                            the maximum health points
      * @param modifiersToAdd the modifiers to add to the new status
      * @param alterationsToAdd the alterations to add to the new status
      * @param alterationsToRemove the alterations to remove (if present) from the
      *                            original status
      * @return
      */
    def updatedStatus(originalStatus: Status, healthPointsToClamp: Int)(
        implicit modifiersToAdd: Map[String, Modifier],
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
