package model

import org.scalatest.FunSuite
import utilities.ScalaProlog.getCharacter

class MoveTypeTest extends FunSuite {
  val meleeType = MoveType("Melee")
  val rangedType = MoveType("Ranged")
  val spellType = MoveType("Spell")


  val warriorCharacter = getCharacter("Jacob")
  val wizardCharacter = getCharacter("Lidya")

  test("Newly created MoveTypes should have the correct representation") {
    assert(
      (meleeType.representation equals "Melee") &&
        (rangedType.representation equals "Ranged") &&
        (spellType.representation equals "Spell"))
  }

  test("A MoveType created with the wrong representation should throw an IllegalArgumentException"){
    val exceptionType = intercept[IllegalArgumentException]{
      MoveType("Except")
    }
    assert(exceptionType.getMessage == "Unknown move type: Except")
  }

  test("The MoveType melee should take into account the correct defence SubStatistic of the target") {
    assert(meleeType.defendingBonus(warriorCharacter) == warriorCharacter.physicalDefence &&
    meleeType.defendingBonus(wizardCharacter) == wizardCharacter.physicalDefence)
  }

  test("The MoveType ranged should take into account the correct defence SubStatistic of the target") {
    assert(rangedType.defendingBonus(warriorCharacter) == warriorCharacter.physicalDefence &&
      rangedType.defendingBonus(wizardCharacter) == wizardCharacter.physicalDefence)
  }

  test("The MoveType spell should take into account the correct defence SubStatistic of the target") {
    assert(spellType.defendingBonus(warriorCharacter) == warriorCharacter.magicalDefence &&
      spellType.defendingBonus(wizardCharacter) == wizardCharacter.magicalDefence)
  }


  test("The MoveType melee should take into account the correct damage SubStatistic of the user") {
    assert(meleeType.attackingBonus(warriorCharacter) == warriorCharacter.physicalDamage &&
      meleeType.attackingBonus(wizardCharacter) == wizardCharacter.physicalDamage)
  }
  test("The MoveType ranged should take into account the correct damage SubStatistic of the user") {
    assert(rangedType.attackingBonus(warriorCharacter) == warriorCharacter.physicalDamage &&
      rangedType.attackingBonus(wizardCharacter) == wizardCharacter.physicalDamage)
  }
  test("The MoveType spell should take into account the correct damage SubStatistic of the user") {
    assert(spellType.attackingBonus(warriorCharacter) == warriorCharacter.magicalPower &&
      spellType.attackingBonus(wizardCharacter) == wizardCharacter.magicalPower)
  }

  test("The MoveType melee should take into account the correct attacking critical bonus of the user") {
    assert(meleeType.attackingCriticalBonus(warriorCharacter) == warriorCharacter.physicalCriticalDamage &&
      meleeType.attackingCriticalBonus(wizardCharacter) == wizardCharacter.physicalCriticalDamage)
  }
  test("The MoveType ranged should take into account the correct attacking critical bonus of the user") {
    assert(rangedType.attackingCriticalBonus(warriorCharacter) == warriorCharacter.physicalCriticalDamage &&
      rangedType.attackingCriticalBonus(wizardCharacter) == wizardCharacter.physicalCriticalDamage)
  }
  test("The MoveType spell should take into account the correct attacking critical bonus of the user") {
    assert(spellType.attackingCriticalBonus(warriorCharacter) == warriorCharacter.magicalCriticalPower &&
      spellType.attackingCriticalBonus(wizardCharacter) == wizardCharacter.magicalCriticalPower)
  }

}
