package model

case class Status(healthPoints: Int,
                  manaPoints: Int,
                  maxHealthPoints: Int,
                  maxManaPoints: Int,
                  modifiers: Map[String, Modifier],
                  afflictions: Map[Affliction, Int])

object Status {

  def nextRound(s: Status): Status =
    s.copy(
      modifiers = s.modifiers
        .mapValues(v => v.copy(roundsDuration = v.roundsDuration - 1))
        .filter(kv => kv._2.roundsDuration > 0),
      afflictions = s.afflictions.mapValues(v => v - 1).filter(kv => kv._2 > 0)
    )

  def consumingMana(s: Status, mp: Int): Status = s.copy(manaPoints = s.manaPoints - mp)

  def afterEffect(s: Status, me: MoveEffect): Status = ???

}
