package model

import scala.util.Random

/** A factory of the possible move effects present in the game. */
object MoveEffectStrategies {

  val minDamage = 1

  import model.Move._
  import StrategiesHelper._



  /** Provides a standard damage effect that describes how to:
    * 1) Decrease the target's health points.
    * 2) Possibly affect the modifiers and the alterations of the target's status.
    *
    * @param moveType the move type
    * @param baseDamage the base damage value
    * @param addModifiers the modifiers to add (or to update if already present)
    *                     to the target's status
    * @param addAlterations the alterations to add (or to update if already present)
    *                       to the target's status
    * @param removeAlterations the alterations to remove (if present) from the
    *                          target's status
    * @return the move effect modeling an appropriate standard damage
    */
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

  /** Provides a standard heal effect that describes how to:
    * 1) Increase the target's health points.
    * 2) Possibly affect the modifiers and the alterations of the target's status.
    *
    * @param baseHeal the base heal value
    * @param addModifiers the modifiers to add (or to update if already present)
    *                     to the target's status
    * @param addAlterations the alterations to add (or to update if already present)
    *                       to the target's status
    * @param removeAlterations the alterations to remove (if present) from the
    *                          target's status
    * @return the move effect modeling an appropriate standard healing
    */
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

  /** Provides a percentage move effect that describes how to:
    * 1) Increase/decrease the target's health points by a certain percentage.
    * 2) Possibly affect the modifiers and the alterations of the target's status.
    *
    * @param percentage the percentage value that will be multiplied by the health
    *                   points of the target (formula: hp * percentage / 100)
    * @param addModifiers the modifiers to add (or to update if already present)
    *                     to the target's status
    * @param addAlterations the alterations to add (or to update if already present)
    *                       to the target's status
    * @param removeAlterations the alterations to remove (if present) from the
    *                          target's status
    * @return the move effect modeling an appropriate percentage modification
    */
  def createPercentageEffect(percentage: Int)(implicit addModifiers: Map[String, Modifier],
                                              addAlterations: Map[Alteration, Int],
                                              removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) =>
      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints * percentage / 100)

  /** Provides a buff/debuff move effect: this type of effect only affects the
    * modifiers and the alterations of the target's status.
    *
    * @param addModifiers the modifiers to add (or to update if already present)
    *                     to the target's status
    * @param addAlterations the alterations to add (or to update if already present)
    *                       to the target's status
    * @param removeAlterations the alterations to remove (if present) from the
    *                          target's status
    * @return the move effect modeling an appropriate buff/debuff modification
    */
  def createBuffDebuffEffect(implicit addModifiers: Map[String, Modifier],
                             addAlterations: Map[Alteration, Int],
                             removeAlterations: Set[Alteration]) =
    (_: Attacker, target: Target) =>
      updatedStatus(originalStatus = target.status, healthPointsToClamp = target.status.healthPoints)

  private object StrategiesHelper {

    /** Determines the actual value, starting from the base value and evaluating
      * if the critical bonus must be applied or not.
      *
      * @param value the base value (base damage or base heal)
      * @param criticalChance the probability to strike a critical move
      * @param criticalBonus the multiplicative factor to apply to the base value
      *                      in case the move is evaluated as critical
      * @return the actual value (equal to the base value if the move is evaluated
      *         as not critical, equal to base value multiplied to the critical
      *         bonus otherwise)
      */
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
      * @param originalStatus the original status before the move effect
      * @param healthPointsToClamp the new health points to clamp between zero and
      *                            the maximum possible health points
      * @param modifiersToAdd the modifiers to add (or to update if already present)
      *                       to the original status
      * @param alterationsToAdd the alterations to add (or to update if already
      *                         present) to the original status
      * @param alterationsToRemove the alterations to remove (if present) from the
      *                            original status
      * @return the updated status
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
