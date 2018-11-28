package model

case class ImmutableStatus(healthPoints: Int,
                           manaPoints: Int,
                           maxHealthPoints: Int,
                           maxManaPoints: Int,
                           modifiers: List[Modifier],
                           afflictions: List[Affliction])

object ImmutableStatus {

  def evaluateNextRoundStatus(currentStatus: ImmutableStatus): Status = ???
  
  def +(status: Status, moveEffect: MoveEffect): Status = ???

}
