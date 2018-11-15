package model

import org.scalatest.FunSuite
import utilities.Utility

class CharacterTest extends FunSuite {
  private val stats =  Statistics(51,33,13,5,27) //This will be taken by a prolog file
  private val warrior =  CharacterFactory("Warrior","Jacob",stats)
  //private val thiefFactory =  CharacterFactory("Thief","Annabelle",34,46,22,8,27)
  //private val wizardFactory =  CharacterFactory("Wizard","Lidya",7,22,34,58,23)
  //private val healerFactory =  CharacterFactory("Healer","Albert",19,11,33,54,27)

  test("testApply") {


    assert(warrior.charName equals "Jacob")

    assert(warrior.stats.strength == 51)
    assert(warrior.stats.agility == 33)
    assert(warrior.stats.spirit == 13)
    assert(warrior.stats.intelligence == 5)
    assert(warrior.stats.resistance == 27)
  }
  test("test the right calculation of sub-statistics"){
    assert(warrior.getPhysDamage == 102)
    assert(warrior.getPysCritDamage == 170)

    assert(warrior.getSpeed == 3)
    assert(warrior.getCritChance == 16)

    assert(warrior.getMagDefence == 13)
    assert(warrior.getMaxMP == 65)

    assert(warrior.getMagDamage == 2)
    assert(warrior.getMagicCritDamage == 150)

    assert(warrior.getMaxHP == 540)
    assert(warrior.getPhysDefence == 40)
  }
  test("The initial health and mp should be equal to it's maximum value when created"){
    assert(warrior.currHP == warrior.getMaxHP) //540
    assert(warrior.currMP == warrior.getMaxMP) //65
  }
  test("Adding and removing health or mp shouldn't exceede the maximum value or the minimum value(0)"){
    warrior.changeCurrHPMP("HP",Utility.sub,40)
    warrior.changeCurrHPMP("MP",Utility.sub,25)

    assert(warrior.currHP== 500)
    assert(warrior.currMP == 40)
    warrior.changeCurrHPMP("HP",Utility.add,60)
    warrior.changeCurrHPMP("MP",Utility.sub,60)

    assert(warrior.currHP == 540)
    assert(warrior.currMP == 0)
  }

}
