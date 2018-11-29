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

    assert(warrior.characterName equals "Jacob")

    assert(warrior.statistics.strength == 51)
    assert(warrior.statistics.agility == 33)
    assert(warrior.statistics.spirit == 13)
    assert(warrior.statistics.intelligence == 5)
    assert(warrior.statistics.resistance == 27)
  }
  test("test the right calculation of sub-statistics") {
    assert(warrior.calculatePhysicalDamage == 102)
    assert(warrior.calculatePysicalCriticalDamage == 170)

    assert(warrior.calculateSpeed == 3)
    assert(warrior.calculateCriticalChance == 16)

    assert(warrior.calculateMagicalDefence == 13)
    assert(warrior.maxMP == 65)

    assert(warrior.calculateMagicalDamage == 2)
    assert(warrior.calculateMagicalCriticalDamage == 150)

    assert(warrior.maxHP == 540)
    assert(warrior.calculatePhysicalDefence == 40)
  }
  test("The initial health and mp should be equal to it's maximum value when created") {
    assert(warrior.status.currentHP == warrior.maxHP) //540
    assert(warrior.status.currentMP == warrior.maxMP) //65
  }
  test("Adding and removing health or mp shouldn't exceede the maximum value or the minimum value(0)") {
    warrior.status.changeCurrHPMP("HP", sub, 40)
    warrior.status.changeCurrHPMP("MP", sub, 25)

    assert(warrior.status.currentHP == 500)
    assert(warrior.status.currentMP == 40)
    warrior.status.changeCurrHPMP("HP", add, 60)
    warrior.status.changeCurrHPMP("MP", sub, 60)

    assert(warrior.status.currentHP == 540)
    assert(warrior.status.currentMP == 0)
  }

  test("Different character should have a different name") {
    assert(!(warrior.characterName equals thief.characterName))
  }
  test("Other classes should have the right statistics") {
    assert(thief.calculatePhysicalDamage == 51)
    assert(thief.calculatePysicalCriticalDamage == 160)

    assert(thief.calculateSpeed == 9)
    assert(thief.calculateCriticalChance == 46)

    assert(thief.calculateMagicalDefence == 22)
    assert(thief.maxMP == 110)

    assert(thief.calculateMagicalDamage == 4)
    assert(thief.calculateMagicalCriticalDamage == 150)

    assert(thief.maxHP == 405)
    assert(thief.calculatePhysicalDefence == 27)
  }
}
