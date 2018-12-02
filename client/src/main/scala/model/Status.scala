package model

case class Status(healthPoints: Int,
                  manaPoints: Int,
                  maxHealthPoints: Int,
                  maxManaPoints: Int,
                  modifiers: Map[String, Modifier],
                  afflictions: Map[Affliction, Int])

object Status {

  def afterAlteration(status: Status, alteration: StatusAlteration): Status = ???

  def afterManaConsumption(status: Status, manaPoints: Int): Status =
    status.copy(manaPoints = status.manaPoints - manaPoints)

  def afterRound(status: Status): Status = tick(afterAfflictionsAlteration(status))

  private def tick(s: Status): Status =
    s.copy(
      modifiers = s.modifiers
        .mapValues(v => v.copy(roundsDuration = v.roundsDuration - 1))
        .filter(kv => kv._2.roundsDuration > 0),
      afflictions = s.afflictions.mapValues(v => v - 1).filter(kv => kv._2 > 0)
    )

  private def afterAfflictionsAlteration(status: Status): Status = ???

}
