package model

case class Status(healthPoints: Int,
                  manaPoints: Int,
                  maxHealthPoints: Int,
                  maxManaPoints: Int,
                  modifiers: Map[String, Modifier],
                  alterations: Map[Alteration, Int])

object Status {

  type NewStatus = Status

  def afterAfflictionsAlterations(status: Status): NewStatus =
    status.alterations
      .map { case (alteration, _) => alteration.beginTurnStatusVariation }
      .filter(_.isDefined)
      .map(_.get)
      .foldLeft(status)((s, beginTurnVariation) => beginTurnVariation(s))

  def afterRoundEnding(status: Status): NewStatus =
    status.copy(
      modifiers = status.modifiers
        .mapValues(v => v.copy(roundsDuration = v.roundsDuration - 1))
        .filter { case (_, modifier) => modifier.roundsDuration > 0 },
      alterations = status.alterations
        .mapValues(v => v - 1)
        .filter { case (_, countDown) => countDown > 0 }
    )

}
