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
  private val jacob = Character("Warrior", "Jacob", jacobStats)
  private val annabelle = Character("Thief", "Annabelle", annabelleStats)

  test("test character creation") {

    assert(jacob.characterName equals "Jacob")

    assert(jacob.statistics.strength == 51)
    assert(jacob.statistics.agility == 33)
    assert(jacob.statistics.spirit == 13)
    assert(jacob.statistics.intelligence == 5)
    assert(jacob.statistics.resistance == 27)
  }
  test("test the right calculation of sub-statistics") {
    assert(jacob.physicalDamage == 102)
    assert(jacob.physicalCriticalDamage == 170)

    assert(jacob.speed == 3)
    assert(jacob.criticalChance == 16)

    assert(jacob.magicalDefence == 13)
    assert(jacob.status.maxManaPoints == 65)

    assert(jacob.magicalPower == 2)
    assert(jacob.magicalCriticalPower == 150)

    assert(jacob.status.maxHealthPoints == 540)
    assert(jacob.physicalDefence == 40)
  }

  test("The initial health and mp should be equal to it's maximum value when created") {
    assert(jacob.status.healthPoints == jacob.status.maxHealthPoints) //540
    assert(jacob.status.manaPoints == jacob.status.maxManaPoints) //65
  }

  test("Different character should have a different name") {
    assert(!(jacob.characterName equals annabelle.characterName))
  }

  test("Other classes should have the right statistics") {
    assert(annabelle.physicalDamage == 51)
    assert(annabelle.physicalCriticalDamage == 160)

    assert(annabelle.speed == 9)
    assert(annabelle.criticalChance == 46)

    assert(annabelle.magicalDefence == 22)
    assert(annabelle.status.maxManaPoints == 110)

    assert(annabelle.magicalPower == 4)
    assert(annabelle.magicalCriticalPower == 150)

    assert(annabelle.status.maxHealthPoints == 405)
    assert(annabelle.physicalDefence == 27)
  }
}
