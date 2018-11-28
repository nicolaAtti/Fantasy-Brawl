package model

case class MoveEffect(deltaHealthPoints: Int,
                      deltaManaPoints: Int,
                      modifiersToAdd: List[Modifier],
                      afflictionsToAdd: List[Affliction])
