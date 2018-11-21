package model

import org.scalatest.FunSuite

class ModifierTest extends FunSuite{
  val jacobStats = Statistics(51,33,13,5,27)
  val warrior = Character("Warrior","Jacob",jacobStats)

  val newModifier = Modifier("PHYS_DAMAGE",3,20)
  val anotherModifier = Modifier("PHYS_DAMAGE",2,40)

  test("Test the correct creation of the modifier"){
    assert(newModifier.affectedStat equals("PHYS_DAMAGE"))
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

}
