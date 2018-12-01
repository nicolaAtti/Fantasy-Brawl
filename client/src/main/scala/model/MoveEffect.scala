package model

case class MoveEffect(deltaHealthPoints: Int,
                      deltaManaPoints: Int,
                      causesModifiers: Map[String, Modifier],
                      causesAfflictions: Map[Affliction, Int])
