package model

case class StatusAlteration(healthPoints: Int => Int,
                            manaPoints: Int => Int,
                            newModifiers: Map[String, Modifier],
                            newAfflictions: Map[Affliction, Int])
