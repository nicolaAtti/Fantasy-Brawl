package model

import org.scalatest.FunSuite

/** *
  * Test for the Character class, and classes composing it
  *
  * @author Nicola Atti
  */
class CharacterTest extends FunSuite {
  import utilities.ScalaProlog._
  private val jacob = getCharacter("Jacob")
  private val annabelle = getCharacter("Annabelle")

  test("New character should have his name and statistics at the creation") {
    assert(
      jacob.characterName.equals("Jacob") &&
        jacob.statistics.strength == 51 &&
        jacob.statistics.agility == 33 &&
        jacob.statistics.spirit == 13 &&
        jacob.statistics.intelligence == 5 &&
        jacob.statistics.resistance == 27)
  }

  test("New character should have the right sub-statistics") {
    assert(
      jacob.physicalDamage == 102 &&
        jacob.physicalCriticalDamage == 170 &&
        jacob.speed == 4 &&
        jacob.criticalChance == 16 &&
        jacob.magicalDefence == 13 &&
        jacob.status.maxManaPoints == 65 &&
        jacob.magicalPower == 2 &&
        jacob.magicalCriticalPower == 150 &&
        jacob.status.maxHealthPoints == 540 &&
        jacob.physicalDefence == 40)
  }

  test("New character should have his special moves") {
    val jacobSpecialMovesName = List("Skullcrack", "Sismic Slam", "Berserker Rage", "Second Wind")
    assert(jacob.specialMoves
      .map{ case (specialMoveName, _) => jacobSpecialMovesName.contains(specialMoveName)}
      .forall(_ == true))
  }

  test("New character should not have modifiers or alterations") {
    assert(
      jacob.status.modifiers.isEmpty &&
        jacob.status.alterations.isEmpty)
  }

  test("The initial health and mp should be equal to its maximum value when created") {
    assert(
      jacob.status.healthPoints == jacob.status.maxHealthPoints && //540
        jacob.status.manaPoints == jacob.status.maxManaPoints) //65
  }

  test("Different character should have a different name") {
    assert(!(jacob.characterName equals annabelle.characterName))
  }

  test("Other classes should have the right statistics") {
    assert(
      annabelle.physicalDamage == 51 &&
        annabelle.physicalCriticalDamage == 160 &&
        annabelle.speed == 10 &&
        annabelle.criticalChance == 46 &&
        annabelle.magicalDefence == 22 &&
        annabelle.status.maxManaPoints == 110 &&
        annabelle.magicalPower == 4 &&
        annabelle.magicalCriticalPower == 150 &&
        annabelle.status.maxHealthPoints == 405 &&
        annabelle.physicalDefence == 27)
  }
}
