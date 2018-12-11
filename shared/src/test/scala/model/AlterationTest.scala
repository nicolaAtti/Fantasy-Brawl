package model

import org.scalatest.FunSuite

class AlterationTest extends FunSuite {
  val stunned = Alteration("Stunned")
  test("The stunned alteration should inhibit all moves") {
    assert(
      stunned.inhibits(PhysicalAttack) &&
        stunned.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        stunned.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        stunned.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        stunned.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The stunned alteration should last 1 turn") {
    assert(stunned.turnDuration == 1)
  }
  test("The stunned alteration should not cause status variations") {
    assert(stunned.beginTurnStatusVariation.isEmpty)
  }

  val asleep = Alteration("Asleep")
  test("The asleep alteration should inhibit all moves") {
    assert(
      asleep.inhibits(PhysicalAttack) &&
        asleep.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        asleep.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        asleep.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        asleep.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The asleep alteration should last 3 turns") {
    assert(asleep.turnDuration == 3)
  }
  test("The asleep alteration should not cause status variations") {
    assert(asleep.beginTurnStatusVariation.isEmpty)
  }

  val poisoned = Alteration("Poisoned")
  test("The poisoned alteration should not inhibit any move") {
    assert(
      !poisoned.inhibits(PhysicalAttack) &&
        !poisoned.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        !poisoned.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        !poisoned.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        !poisoned.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The poisoned alteration should last 3 turns") {
    assert(poisoned.turnDuration == 3)
  }
  test("The poisoned alteration should cause status variations") {
    assert(poisoned.beginTurnStatusVariation.isDefined)
  }

  val regeneration = Alteration("Regeneration")
  test("The regeneration alteration should not inhibit any move") {
    assert(
      !regeneration.inhibits(PhysicalAttack) &&
        !regeneration.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        !regeneration.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        !regeneration.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        !regeneration.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The regeneration alteration should last 3 turns") {
    assert(regeneration.turnDuration == 2)
  }
  test("The regeneration alteration should cause status variations") {
    assert(regeneration.beginTurnStatusVariation.isDefined)
  }

  val berserk = Alteration("Berserk")
  test("The berserk alteration should inhibit all moves except the physical attack") {
    assert(
      !berserk.inhibits(PhysicalAttack) &&
        berserk.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        berserk.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        berserk.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        berserk.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The berserk alteration should last 3 turns") {
    assert(berserk.turnDuration == 3)
  }
  test("The berserk alteration should not cause status variations") {
    assert(berserk.beginTurnStatusVariation.isEmpty)
  }

  val silenced = Alteration("Silenced")
  test("The silenced alteration should inhibit only spell moves") {
    assert(
      !silenced.inhibits(PhysicalAttack) &&
        !silenced.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        silenced.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        !silenced.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        silenced.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The silenced alteration should last 2 turns") {
    assert(silenced.turnDuration == 2)
  }
  test("The silenced alteration should not cause status variations") {
    assert(silenced.beginTurnStatusVariation.isEmpty)
  }

  val frozen = Alteration("Frozen")
  test("The frozen alteration should inhibit only melee moves") {
    assert(
      frozen.inhibits(PhysicalAttack) &&
        frozen.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        !frozen.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        !frozen.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        !frozen.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The frozen alteration should last 2 turns") {
    assert(frozen.turnDuration == 2)
  }
  test("The frozen alteration should not cause status variations") {
    assert(frozen.beginTurnStatusVariation.isEmpty)
  }

  val blinded = Alteration("Blinded")
  test("The blinded alteration should inhibit only ranged moves") {
    assert(
      !blinded.inhibits(PhysicalAttack) &&
        !blinded.inhibits(SpecialMove("SpecialMoveName", "StandardDamage", MoveType("Melee"), 0, Map(), Map(), Set(), 0, 1)) &&
        !blinded.inhibits(SpecialMove("SpecialMoveName", "StandardHeal", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)) &&
        blinded.inhibits(SpecialMove("SpecialMoveName", "Percentage", MoveType("Ranged"), 0, Map(), Map(), Set(), 0, 1)) &&
        !blinded.inhibits(SpecialMove("SpecialMoveName", "BuffDebuff", MoveType("Spell"), 0, Map(), Map(), Set(), 0, 1)))
  }
  test("The blinded alteration should last 2 turns") {
    assert(blinded.turnDuration == 2)
  }
  test("The blinded alteration should not cause status variations") {
    assert(blinded.beginTurnStatusVariation.isEmpty)
  }
}
