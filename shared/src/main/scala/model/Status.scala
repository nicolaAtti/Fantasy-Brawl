package model

case class Status(healthPoints: Int,
                  manaPoints: Int,
                  maxHealthPoints: Int,
                  maxManaPoints: Int,
                  modifiers: Map[String, Modifier],
                  afflictions: Map[Affliction, Int])

object Status {

  type NewStatus = Status

  def afterAfflictionsAlterations(status: Status): NewStatus =
    status.afflictions
      .map(kv => kv._1.beginTurnAlteration)
      .filter(_.isDefined)
      .map(_.get)
      .foldLeft(status)((s, alteration) => alteration(s))

  def afterRoundEnding(status: Status): NewStatus =
    status.copy(
      modifiers = status.modifiers
        .mapValues(v => v.copy(roundsDuration = v.roundsDuration - 1))
        .filter(kv => kv._2.roundsDuration > 0),
      afflictions = status.afflictions.mapValues(v => v - 1).filter(kv => kv._2 > 0)
    )

}
