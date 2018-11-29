package model

case class MoveEffect(deltaHealthPoints: Int,
                      deltaManaPoints: Int,
                      causesModifiers: Map[String, ImmutableModifier],
                      causesAfflictions: Map[ImmutableAffliction, Int])
