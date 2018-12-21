package model

import org.scalatest.FunSuite
import utilities.ScalaProlog._

class MoveTest extends FunSuite {
  val physicalAttack = PhysicalAttack

  val specialMelee = getSpecialMove("Holy Smite")
  val specialRanged = getSpecialMove("Aimed Shot")
  val specialSpell = getSpecialMove("Lay On Hands")
  val multiAlterationMove = getSpecialMove("Benefic Spores")
  val multiModifierMove = getSpecialMove("Bolster Faith")
  val multiTargetMove = getSpecialMove("Flamestrike")
  val percentageMove = getSpecialMove("Chrono Shift")
  val percentageHeal = getSpecialMove("Second Wind")

  val stunned = Alteration("Stn")
  val frozen = Alteration("Frz")
  val silenced = Alteration("Sil")
  val blinded = Alteration("Bln")
  val berserk = Alteration("Brk")
  val asleep = Alteration("Slp")
  val regeneration = Alteration("Reg")

  val stunnedStatus = Status(200, 100, 200, 100, Map(), Map((stunned, stunned.roundsDuration)))
  val frozenStatus = Status(200, 100, 200, 100, Map(), Map((frozen, frozen.roundsDuration)))
  val silencedStatus = Status(200, 100, 200, 100, Map(), Map((silenced, silenced.roundsDuration)))
  val blindedStatus = Status(200, 100, 200, 100, Map(), Map((blinded, blinded.roundsDuration)))
  val berserkStatus = Status(200, 100, 200, 100, Map(), Map((berserk, berserk.roundsDuration)))
  val asleepStatus = Status(200, 100, 200, 100, Map(), Map((asleep, asleep.roundsDuration)))

  val lowManaStatus = Status(200, 10, 200, 100, Map(), Map())

  test("The move Physical Attack should have the correct parameters") {
    assert(
      physicalAttack.moveType == MoveType.Melee &&
        physicalAttack.manaCost == 0 &&
        physicalAttack.maxTargets == 1)
  }

  test("A special move should have the right parameters") {
    assert(
      specialSpell.moveType == MoveType.Spell &&
        specialSpell.manaCost == 20 &&
        specialSpell.maxTargets == 1)
  }

  test("The mana cost of a special move must be a positive number") {
    val wrongSpecialMove = intercept[IllegalArgumentException] {
      SpecialMove("Wrong",
                  MoveType("Melee"),
                  MoveEffectStrategies.createStandardDamageEffect(MoveType.Melee, 0)(Map(), Map(), Set()),
                  -10,
                  1)
    }
    assert(wrongSpecialMove.getMessage == "requirement failed: The mana cost cannot be negative")
  }
  test("The number of targets of a special move must be a positive number") {
    val wrongSpecialMove = intercept[IllegalArgumentException] {
      SpecialMove("Wrong",
                  MoveType("Melee"),
                  MoveEffectStrategies.createStandardDamageEffect(MoveType.Melee, 0)(Map(), Map(), Set()),
                  10,
                  -1)
    }
    assert(wrongSpecialMove.getMessage == "requirement failed: The number of maximum targets must be at least one")
  }

  test("A stunned character cannot do any move") {
    val character = getCharacter("Jacob")
    character.status = stunnedStatus
    assert(
      !Move.canMakeMove(character, physicalAttack) && !Move.canMakeMove(character, specialMelee) && !Move
        .canMakeMove(character, specialRanged) && !Move.canMakeMove(character, specialSpell))
  }

  test("A frozen character cannot do any melee move") {
    val character = getCharacter("Jacob")
    character.status = frozenStatus
    assert(
      !Move.canMakeMove(character, physicalAttack) && !Move.canMakeMove(character, specialMelee) && Move
        .canMakeMove(character, specialRanged) && Move.canMakeMove(character, specialSpell))
  }
  test("A silenced character cannot do any spell") {
    val character = getCharacter("Jacob")
    character.status = silencedStatus
    assert(
      Move.canMakeMove(character, physicalAttack) && Move.canMakeMove(character, specialMelee) && Move
        .canMakeMove(character, specialRanged) && !Move.canMakeMove(character, specialSpell))
  }
  test("A blinded character cannot do any ranged move") {
    val character = getCharacter("Jacob")
    character.status = blindedStatus
    assert(
      Move.canMakeMove(character, physicalAttack) && Move.canMakeMove(character, specialMelee) && !Move
        .canMakeMove(character, specialRanged) && Move.canMakeMove(character, specialSpell))
  }
  test("A bersek character cannot do any move apart from the physical attack") {
    val character = getCharacter("Jacob")
    character.status = berserkStatus
    assert(
      Move.canMakeMove(character, physicalAttack) && !Move.canMakeMove(character, specialMelee) && !Move
        .canMakeMove(character, specialRanged) && !Move.canMakeMove(character, specialSpell))
  }
  test("An asleep character cannot do any move") {
    val character = getCharacter("Jacob")
    character.status = asleepStatus
    assert(
      !Move.canMakeMove(character, physicalAttack) && !Move.canMakeMove(character, specialMelee) && !Move
        .canMakeMove(character, specialRanged) && !Move.canMakeMove(character, specialSpell))
  }
  test("A character cannot make a special move that costs more than his actual mana") {
    val character = getCharacter("Jacob")
    character.status = lowManaStatus
    assert(Move.canMakeMove(character, specialMelee) && !Move.canMakeMove(character, specialSpell))
  }

  test("A move against another character should yield the correct status reflecting the move effect") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val newStatuses = Move.makeMove(physicalAttack, userCharacter, Set(targetCharacter))
    assert(
      (newStatuses(targetCharacter).healthPoints == 969 || newStatuses(targetCharacter).healthPoints == 933) && !newStatuses
        .contains(userCharacter))
  }

  test(
    "A special move against another character should yield the correct status for the target, and also another status reflecting the loss of mana for the user") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val newStatuses = Move.makeMove(specialMelee, userCharacter, Set(targetCharacter))
    assert(
      (newStatuses(targetCharacter).healthPoints == 954 || newStatuses(targetCharacter).healthPoints == 908) && newStatuses(
        userCharacter).manaPoints == 90)
  }

  test("Using an healing spell of themselves should produce only one status with the combined health and mana changes") {
    val damagedStatus = Status(10, 100, 300, 100, Map(), Map())
    val healerCharacter = getCharacter("Albert")
    healerCharacter.status = damagedStatus
    val newStatuses = Move.makeMove(specialSpell, healerCharacter, Set(healerCharacter))
    assert(
      (newStatuses(healerCharacter).healthPoints == 178 || newStatuses(healerCharacter).healthPoints == 279) && newStatuses(
        healerCharacter).manaPoints == 80)
  }

  test(
    "Using a special move on multiple targets should return a new status for each of them (with different changes according to target original status and statistics)") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val healerCharacter = getCharacter("Albert")
    val anotherCharacter = getCharacter("Annabelle")
    val wizardCharacter = getCharacter("Linn")
    val newStatuses = Move.makeMove(multiTargetMove,
                                    wizardCharacter,
                                    Set(userCharacter, targetCharacter, healerCharacter, anotherCharacter))
    assert(
      (newStatuses(userCharacter).healthPoints == 610 || newStatuses(userCharacter).healthPoints == 523) &&
        (newStatuses(targetCharacter).healthPoints == 892 || newStatuses(targetCharacter).healthPoints == 813) &&
        (newStatuses(healerCharacter).healthPoints == 573 || newStatuses(healerCharacter).healthPoints == 494) &&
        (newStatuses(anotherCharacter).healthPoints == 537 || newStatuses(anotherCharacter).healthPoints == 451) &&
        (newStatuses(wizardCharacter).manaPoints == 175))
  }

  test("Using a damaging move should remove the Asleep alteration from it's targets") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    targetCharacter.status = asleepStatus
    val newStatuses = Move.makeMove(physicalAttack, userCharacter, Set(targetCharacter))
    assert(
      targetCharacter.status.alterations.contains(asleep) && !newStatuses(targetCharacter).alterations.contains(asleep))
  }

  test("A special move that applies an alteration, should apply it to all it's targets") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val healerCharacter = getCharacter("Albert")
    val wizardCharacter = getCharacter("Linn")

    val newStatuses = Move.makeMove(multiModifierMove,
                                    wizardCharacter,
                                    Set(userCharacter, targetCharacter, healerCharacter, wizardCharacter))
    assert(newStatuses.forall(chars => chars._2.modifiers.contains(getModifier("Bolstered Faith"))))
  }

  test("A special move that applies a modifier, should apply it to all it's targets") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val healerCharacter = getCharacter("Albert")
    val wizardCharacter = getCharacter("Linn")

    val newStatuses = Move.makeMove(multiAlterationMove,
                                    wizardCharacter,
                                    Set(userCharacter, targetCharacter, healerCharacter, wizardCharacter))
    assert(newStatuses.forall(chars => chars._2.alterations.contains(regeneration)))
  }

  test("A special move that deals percentage damage should deal the right amount of damage regardless of defences") {
    val userCharacter = getCharacter("Jacob")
    val targetCharacter = getCharacter("Cassandra")
    val healerCharacter = getCharacter("Albert")
    val wizardCharacter = getCharacter("Linn")
    val anotherCharacter = getCharacter("Annabelle")

    val newStatuses = Move.makeMove(percentageMove,wizardCharacter,Set(userCharacter,targetCharacter,healerCharacter,anotherCharacter))
    assert(newStatuses(userCharacter).healthPoints == 562 &&
            newStatuses(targetCharacter).healthPoints == 765 &&
            newStatuses(healerCharacter).healthPoints == 525 &&
            newStatuses(anotherCharacter).healthPoints == 506)
  }
  test("A special move that heals for a percentage of the current health should heal for the correct amount"){
    val userCharacter = getCharacter("Jacob")
    val damagedStatus = Status(50, 100, 300, 100, Map(), Map())
    userCharacter.status = damagedStatus
    val newStatuses = Move.makeMove(percentageHeal,userCharacter,Set(userCharacter))
    assert(newStatuses(userCharacter).healthPoints == 87)
  }

}
