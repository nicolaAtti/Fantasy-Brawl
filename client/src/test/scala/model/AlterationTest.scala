package model

import org.scalatest.FunSuite

class AlterationTest extends FunSuite {
  val jacobStats = Statistics(51, 33, 13, 5, 27)
  val jacob = Character("Warrior", "Jacob", jacobStats)

  //var stunned = ImmutableAffliction("Stunned", 1)
  //var poisoned = ImmutableAffliction("Poisoned", 3)
  //var berserk = ImmutableAffliction("Berserk", 3)
  //var silenced = ImmutableAffliction("Silenced", 2)
  //var frozen = ImmutableAffliction("Frozen", 2)
  //var asleep = ImmutableAffliction("Asleep", 3)
  //var blinded = ImmutableAffliction("Blinded", 2)

  test("The afflictions must be correctly istantiated") {
    //assert(stunned.afflictionType equals ("Stunned"))
    //assert(stunned.turnDuration == 1)

    //assert(poisoned.afflictionType equals ("Poisoned"))
    //assert(poisoned.turnDuration == 3)
  }
  /*test("After a round the duration of the affliction should be reduced"){
    jacob.status = jacob.status.copy(afflictions = List(poisoned,stunned))
    jacob.status = ImmutableStatus.afterRound(jacob.status)

    assert(!(jacob.status.afflictions.contains()))
  }*/
}
