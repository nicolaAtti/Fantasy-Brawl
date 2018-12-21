package model

import org.scalatest.FunSuite
import Status._
import utilities.ScalaProlog._

class StatusTest extends FunSuite {

  val status = Status(200, 100, 200, 100, Map(), Map())

  val poison: Alteration = Alteration("Psn")
  val regeneration: Alteration = Alteration("Reg")
  val concentratedModifier: Modifier = getModifier("Concentrated")
  val stunned: Alteration = Alteration("Stn")

  val poisonedStatus = Status(200, 100, 200, 100, Map(), Map((poison, poison.roundsDuration)))
  val regenerateStatus = Status(100, 100, 200, 100, Map(), Map((regeneration, regeneration.roundsDuration)))

  val removeStatus: Status =
    Status(200, 100, 200, 100, Map((concentratedModifier, concentratedModifier.roundsDuration)), Map((stunned, stunned.roundsDuration)))

  test("A newly created status should have correct health values") {
    assert(status.healthPoints == 200 && status.maxHealthPoints == status.healthPoints)
  }

  test("A newly created status should have correct mana values") {
    assert(status.manaPoints == 100 && status.maxManaPoints == status.manaPoints)
  }

  test("A newly created status shouldn't have any Modifier or Alteration") {
    assert(status.alterations == Map() && status.modifiers == Map())
  }

  test("Health points higher than maximum health points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(200, 100, 100, 100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Health points cannot be higher than max health points")
  }
  test("Mana points higher than maximum mana points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(100, 200, 100, 100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Mana points cannot be higher than max mana points")
  }
  test("Negative health points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(-100, 100, 100, 100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Health points cannot be negative")
  }
  test("Negative mana points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(100, -100, 100, 100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Mana points cannot be negative")
  }
  test("Negative maximum health points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(100, 100, -100, 100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Max health points must be positive")
  }
  test("Negative maximum mana points should return an IllegalArgumentException") {
    val wrongStatus = intercept[IllegalArgumentException] {
      Status(100, 100, 100, -100, Map(), Map())
    }
    assert(wrongStatus.getMessage == "requirement failed: Max mana points must be positive")
  }

  test("After using a move, the correct amount of mana should be subtracted") {
    val move = getSpecialMove("Lay On Hands")
    val newStatus = afterManaConsumption(status, move)
    assert(
      newStatus.manaPoints < newStatus.maxManaPoints &&
        newStatus.manaPoints == status.manaPoints - move.manaCost)
  }

  test("After a new turn start, the returned status must register the damage inflicted by the Poison alteration") {
    val afterTurnStatus = afterTurnStart(poisonedStatus)
    assert(afterTurnStatus.healthPoints == 150 && afterTurnStatus.alterations(poison) == 2)
  }
  test("After a new turn start, the returned status must register the healing done by the Regeneration alteration") {
    val afterTurnStatus = afterTurnStart(regenerateStatus)
    assert(afterTurnStatus.healthPoints == 125 && afterTurnStatus.alterations(regeneration) == 1)
  }
  test("After a new turn start, alterations or modifiers that reach duration zero should be removed") {
    var afterTurnStatus = afterTurnStart(removeStatus)
    afterTurnStatus = afterTurnStart(afterTurnStatus)
    assert(afterTurnStatus.alterations == Map() && afterTurnStatus.modifiers == Map())
  }

}
