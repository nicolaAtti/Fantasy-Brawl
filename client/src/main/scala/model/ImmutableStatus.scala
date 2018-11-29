package model

case class ImmutableStatus(healthPoints: Int,
                           manaPoints: Int,
                           maxHealthPoints: Int,
                           maxManaPoints: Int,
                           modifiers: List[ImmutableModifier],
                           afflictions: List[ImmutableAffliction])

object ImmutableStatus {

  def afterRound(status: ImmutableStatus): ImmutableStatus =
    status.copy(modifiers = ImmutableModifier.ticked(status.modifiers),
                afflictions = ImmutableAffliction.ticked(status.afflictions))

  def consumingMana(status: ImmutableStatus, mp: Int): ImmutableStatus =
    status.copy(manaPoints = status.manaPoints - mp)

  def +++(status: Status, moveEffect: MoveEffect): Status = ???

}
