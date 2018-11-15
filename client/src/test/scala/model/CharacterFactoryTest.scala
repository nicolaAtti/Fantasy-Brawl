package model

import org.scalatest.FunSuite
import utilities.Utility

class CharacterFactoryTest extends FunSuite {
  private val stats =  Statistics(51,33,13,5,27) //This will be taken by a prolog file
  private val warriorFactory =  CharacterFactory("Warrior","Jacob",stats)
  //private val thiefFactory =  CharacterFactory("Thief","Annabelle",34,46,22,8,27)
  //private val wizardFactory =  CharacterFactory("Wizard","Lidya",7,22,34,58,23)
  //private val healerFactory =  CharacterFactory("Healer","Albert",19,11,33,54,27)

  test("testApply") {


    assert(warriorFactory.charName equals "Jacob")

    assert(warriorFactory.stats.strength == 51)
    assert(warriorFactory.stats.agility == 33)
    assert(warriorFactory.stats.spirit == 13)
    assert(warriorFactory.stats.intelligence == 5)
    assert(warriorFactory.stats.resistance == 27)
  }
  test("test the right calculation of sub-statistics"){
    assert(warriorFactory.getPhysDamage == 102)
    assert(warriorFactory.getPysCritDamage == 170)

    assert(warriorFactory.getSpeed == 3)
    assert(warriorFactory.getCritChance == 16)

    assert(warriorFactory.getMagDefence == 13)
    assert(warriorFactory.getMaxMP == 65)

    assert(warriorFactory.getMagDamage == 2)
    assert(warriorFactory.getMagicCritDamage == 150)

    assert(warriorFactory.getMaxHP == 540)
    assert(warriorFactory.getPhysDefence == 40)
  }
  test("The initial health and mp should be equal to it's maximum value when created"){
    assert(warriorFactory.currHP == warriorFactory.getMaxHP) //540
    assert(warriorFactory.currMP == warriorFactory.getMaxMP) //65
  }
  test("Adding and removing health or mp shouldn't exceede the maximum value or the minimum value(0)"){
    assert(warriorFactory.changeCurrHPMP("HP",Utility.sub,40) == 500)
    assert(warriorFactory.changeCurrHPMP("MP",Utility.sub,25) == 40)

    assert(warriorFactory.changeCurrHPMP("HP",Utility.add,60) == 540)
    assert(warriorFactory.changeCurrHPMP("MP",Utility.sub,60) == 0)
  }

}
