package model

/** Represents the current status of a character.
  *
  * @param healthPoints the current health points of a character. Health points
  *                     must be non negative and not higher than max health points.
  * @param manaPoints the current mana points of a character. Mana points must be
  *                   non negative and not higher than max mana points.
  * @param maxHealthPoints the maximum health points that a character can have
  * @param maxManaPoints the maximum mana points that a character can have
  * @param modifiers the current modifiers that alter the character's statistics
  * @param alterations the current alterations that affects the character's behaviour
  */
case class Status(healthPoints: Int,
                  manaPoints: Int,
                  maxHealthPoints: Int,
                  maxManaPoints: Int,
                  modifiers: Map[String, Modifier],
                  alterations: Map[Alteration, Int]) {

  require(maxHealthPoints > 0, "Max health points must be positive")
  require(maxManaPoints > 0, "Max mana points must be positive")
  require(healthPoints >= 0, "Health points cannot be negative")
  require(healthPoints <= maxHealthPoints, "Health points cannot be higher than max health points")
  require(manaPoints >= 0, "Mana points cannot be negative")
  require(manaPoints <= maxManaPoints, "Mana points cannot be higher than max mana points")
}

object Status {
  import StatusHelper._

  /** Evaluates the new status, consuming the appropriate amount of mana for the
    * specified move.
    *
    * @param status the status of a character before the mana consumption
    * @param move the move that will possibly consume some mana when executed
    * @return the new status after the mana consumption
    */
  def afterManaConsumption(status: Status, move: Move): Status =
    status.copy(manaPoints = 0 max (status.manaPoints - move.manaCost))

  /** Evaluates the new status after having applied all the alterations effects,
    * then having decremented by one the remaining turns of each modifier and
    * alteration.
    *
    * If the remaining turns of a modifier (or an alteration) reach zero,
    * that modifier (or the alteration) will be removed.
    * This function describes how the status of every character will be modified
    * at the beginning of its turn.
    *
    * @param status the status of a character before the modifications
    * @return the new status after the modifications
    */
  def afterTurnStart(status: Status): Status = (afterAlterationsEffects _ andThen afterTick)(status)

  private object StatusHelper {

    /** Evaluates the new status after having applied all the alterations effects.
      *
      * @param status the status of a character before the alterations
      * @return the new status after the alterations effects
      */
    def afterAlterationsEffects(status: Status): Status =
      status.alterations
        .flatMap { case (alteration, _) => alteration.beginTurnStatusVariation } // 'flatten' takes out the Option and discard None values
        .foldLeft(status)((s, beginTurnVariation) => beginTurnVariation(s))

    /** Evaluates the new status decrementing by one all the counters relative to
      * the remaining turns of each modifier and alteration.
      *
      * If the remaining turns of a modifier (or an alteration) reach zero,
      * that modifier (or the alteration) will be removed.
      *
      * @param status the status of a character before the countdown
      * @return the new status after the countdown
      */
    def afterTick(status: Status): Status =
      status.copy(
        modifiers = status.modifiers
          .mapValues(v => v.copy(remainingRounds = v.remainingRounds - 1))
          .filter { case (_, modifier) => modifier.remainingRounds > 0 },
        alterations = status.alterations
          .mapValues(v => v - 1)
          .filter { case (_, countDown) => countDown > 0 }
      )
  }

}
