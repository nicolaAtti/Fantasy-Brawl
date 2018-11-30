package model

case class ImmutableStatus(healthPoints: Int,
                           manaPoints: Int,
                           maxHealthPoints: Int,
                           maxManaPoints: Int,
                           modifiers: Map[String, ImmutableModifier],
                           afflictions: Map[ImmutableAffliction, Int])

object ImmutableStatus {

  def nextRound(s: ImmutableStatus): ImmutableStatus =
    s.copy(
      modifiers = s.modifiers
        .mapValues(v => v.copy(roundsDuration = v.roundsDuration - 1))
        .filter(kv => kv._2.roundsDuration > 0),
      afflictions = s.afflictions.mapValues(v => v - 1).filter(kv => kv._2 > 0)
    )

  def consumingMana(s: ImmutableStatus, mp: Int): ImmutableStatus =
    s.copy(manaPoints = s.manaPoints - mp)

  def +++(status: Status, moveEffect: MoveEffect): Status = ???

}
