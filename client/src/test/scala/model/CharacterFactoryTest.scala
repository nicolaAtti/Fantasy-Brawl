package model

import main.scala.model.CharacterFactory
import org.scalatest.FunSuite

class CharacterFactoryTest extends FunSuite {
  private val stats = new Statistics(51,33,13,5,27)
  private val warMods =  new StatModifiers(2,1,1,0.5,1.5,2)
  private val warriorFactory =  CharacterFactory("Warrior","Jacob",stats,warMods)
  //private val thiefFactory =  CharacterFactory("Thief","Annabelle",34,46,22,8,27)
  //private val wizardFactory =  CharacterFactory("Wizard","Lidya",7,22,34,58,23)
  //private val healerFactory =  CharacterFactory("Healer","Albert",19,11,33,54,27)

  test("testApply") {


    assert(warriorFactory.charName equals "Jacob")

    assert(warriorFactory.stats.getStrength == 51)
    assert(warriorFactory.stats.getAgility == 33)
    assert(warriorFactory.stats.getSpirit == 13)
    assert(warriorFactory.stats.getIntelligence == 5)
    assert(warriorFactory.stats.getResistance == 27)
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

}
