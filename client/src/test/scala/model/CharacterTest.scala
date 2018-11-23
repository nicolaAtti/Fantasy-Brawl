package model

import org.scalatest.FunSuite
import utilities.Utility._

/** *
  * Test for the Character class, and classes composing it
  *
  * @author Nicola Atti
  */
class CharacterTest extends FunSuite {
  val jacobStats = Statistics(51, 33, 13, 5, 27) //This will be taken by a prolog file
  val annabelleStats = Statistics(34, 46, 22, 8, 27)
  val lidyaStats = Statistics(7, 22, 34, 58, 23)
  val albertStats = Statistics(19, 11, 33, 54, 27)
  private val warrior = Character("Warrior", "Jacob", jacobStats)
  private val thief = Character("Thief", "Annabelle", annabelleStats)
  private val wizard = Character("Wizard", "Lidya", lidyaStats)
  private val healer = Character("Healer", "Albert", albertStats)

  test("test character creation") {

    assert(warrior.charName equals "Jacob")

    assert(warrior.stats.strength == 51)
    assert(warrior.stats.agility == 33)
    assert(warrior.stats.spirit == 13)
    assert(warrior.stats.intelligence == 5)
    assert(warrior.stats.resistance == 27)
  }
  test("test the right calculation of sub-statistics") {
    assert(warrior.getPhysDamage == 102)
    assert(warrior.getPysCritDamage == 170)

    assert(warrior.getSpeed == 3)
    assert(warrior.getCritChance == 16)

    assert(warrior.getMagDefence == 13)
    assert(warrior.maxMP == 65)

    assert(warrior.getMagDamage == 2)
    assert(warrior.getMagicCritDamage == 150)

    assert(warrior.maxHP == 540)
    assert(warrior.getPhysDefence == 40)
  }
  test(
    "The initial health and mp should be equal to it's maximum value when created") {
    assert(warrior.currHP == warrior.maxHP) //540
    assert(warrior.currMP == warrior.maxMP) //65
  }
  test(
    "Adding and removing health or mp shouldn't exceede the maximum value or the minimum value(0)") {
    warrior.changeCurrHPMP("HP", sub, 40)
    warrior.changeCurrHPMP("MP", sub, 25)

    assert(warrior.currHP == 500)
    assert(warrior.currMP == 40)
    warrior.changeCurrHPMP("HP", add, 60)
    warrior.changeCurrHPMP("MP", sub, 60)

    assert(warrior.currHP == 540)
    assert(warrior.currMP == 0)
  }

  test("Different character should have a different name") {
    assert(!(warrior.charName equals thief.charName))
  }
  test("Other classes should have the right statistics") {
    assert(thief.getPhysDamage == 51)
    assert(thief.getPysCritDamage == 160)

    assert(thief.getSpeed == 9)
    assert(thief.getCritChance == 46)

    assert(thief.getMagDefence == 22)
    assert(thief.maxMP == 110)

    assert(thief.getMagDamage == 4)
    assert(thief.getMagicCritDamage == 150)

    assert(thief.maxHP == 405)
    assert(thief.getPhysDefence == 27)
  }
}
