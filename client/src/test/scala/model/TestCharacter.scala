package scala.model

import main.scala.model.{Character,CharacterFactory}
import org.scalatest.{FlatSpec,Matchers}

@RunWith(classOf[JUnitRunner])
class TestCharacter extends FlatSpec {

  val warriorFactory = new CharacterFactory("Warrior","Jacob",51,33,13,5,27)
  val thiefFactory = new CharacterFactory("Thief","Annabelle",34,46,22,8,27)
  val wizardFactory = new CharacterFactory("Wizard","Lidya",7,22,34,58,23)
  val healerFactory = new CharacterFactory("Healer","Albert",19,11,33,54,27)

  test("New character has right statistics") {
    assert(warriorFactory.getSrength == 51)
    assert(warriorFactory.getAgility == 33)
    assert(warriorFactory.getSpirit == 13)
    assert(warriorFactory.getIntelligence == 5)
    assert(warriorFactory.getResistance == 27)
  }

  test("Check the substatistics"){
    assert(warriorFactory.getPhysCritDamage == 170)
    assert(warriorFactory.getPhysdDamage == 102)

    assert(warriorFactory.getSpeed == 3)
    assert(warriorFactory.getCritChance == 16.5)

    assert(warriorFactory.getMagicDefence == 13)
    assert(warriorFactory.getMaxMP == 65)

    assert(warriorFactory.getMagicDamage == 2)
    assert(warriorFactory.getHealPotency == 2)
    assert(warriorFactory.getMagicCritDamage == 150)
    assert(warriorFactory.getHealCritPotency == 2)

    assert(warriorFactory.getMaxHP == 540)
    assert(warriorFactory.getPhysDefence == 40)
  }
  test{"Testare il cambiamento delle statistiche di MP e HP , anche nei casi estremi"}
  //Voglio testare che l'oggetto sia creato correttamente e che le 4 classi gestiscano a partire dai valori delle statistiche base
  // i moltiplicatori in modo giusto,

}
