package model

import org.scalatest.FunSuite

class AfflictionTest extends FunSuite {
  val jacobStats = Statistics(51,33,13,5,27)
  val warrior = Character("Warrior","Jacob",jacobStats)

  var stunned = Affliction("Stunned",1)
  var poisoned = Affliction("Poisoned",3)
  var berserk = Affliction("Berserk",3)
  var silenced = Affliction("Silenced",2)
  var frozen = Affliction("Frozen",2)
  var asleep = Affliction("Asleep",3)
  var blinded = Affliction("Blinded",2)

  test("The afflictions must be correctly istantiated"){
    assert(stunned.afflictionType equals("Stunned"))
    assert(stunned.turnDuration == 1)

    assert(poisoned.afflictionType equals("Poisoned"))
    assert(poisoned.turnDuration == 3)
  }

  test("A poisoned character should lose hp each of his turns"){
    assert(warrior.status.currHP == 540)
    warrior.status.addAffliction(poisoned)
    warrior.status.newTurnCountdown()
    assert(warrior.status.currHP == 405)
  }
  test("Applying an existing affliction should do nothing"){
    warrior.status.addAffliction(stunned)
    assert(warrior.status.afflictions.filter(affl => affl.afflictionType equals "Stunned").length != 2)
  }

  //Add test for all the other afflictions (need moves implementation)

}