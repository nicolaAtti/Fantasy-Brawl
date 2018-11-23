package model

import org.scalatest.FunSuite

/** Test for all the modifier features
  *
  * @author Nicola Atti
  */
class ModifierTest extends FunSuite{
  val jacobStats = Statistics(51,33,13,5,27)
  val warrior = Character("Warrior","Jacob",jacobStats)

  val newModifier = Modifier("battle_prowess","PHYS_DAMAGE",3,20)
  val anotherModifier = Modifier("gargantuan_rage","PHYS_DAMAGE",2,40)

  test("Test the correct creation of the modifier"){
    assert(newModifier.affectedStat equals "PHYS_DAMAGE")
    assert(newModifier.turnDuration == 3)
    assert(newModifier.modValue == 20)
  }

  test("Test the correct application of a new modifier"){
    assert(warrior.getPhysDamage == 102)
    warrior.addModifier(newModifier)
    assert(warrior.getPhysDamage == 122)
  }

  test("Test the application of multiple modifiers"){
    warrior.addModifier(anotherModifier)
    assert(warrior.getPhysDamage == 162)
  }
  test("After a turn the modifiers timer should decrease by 1"){
    warrior.newTurnCountdown()
    assert(newModifier.turnDuration == 2)
    assert(anotherModifier.turnDuration == 1)
  }
  test("The adding of an existing modifier should 'reset' the timer of the previous one") {
    val newNewModifier = Modifier("battle_prowess","PHYS_DAMAGE",3,20)
    warrior.addModifier(newNewModifier)
    assert(warrior.statMods.filter(mod => mod.modId equals newModifier.modId).head.turnDuration == 3)
    assert(warrior.getPhysDamage == 162)
  }

  test("A modifiers that reaches 0 in turnDuration should be removed from the list"){
    warrior.newTurnCountdown()
    assert(anotherModifier.turnDuration == 0)
    assert(warrior.getPhysDamage == 122)
  }
}
