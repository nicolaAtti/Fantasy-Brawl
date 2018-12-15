package utilities

import model.{Character, Modifier, SpecialMove}
import org.scalatest.FunSuite
import utilities.ScalaProlog._

class ScalaPrologTest extends FunSuite {
  test("Extract a character from prolog should return the appropriate character") {
    assert(getCharacter("Jacob").isInstanceOf[Character] &&
      getCharacter("Jacob").characterName == "Jacob")
  }
  test("Extract a special move from prolog should return the appropriate special move") {
    assert(getSpecialMove("Antidote").isInstanceOf[SpecialMove] &&
      getSpecialMove("Antidote").name == "Antidote")
  }
  test("Extract a modifier from prolog should return the appropriate modifier") {
    assert(getModifier("Concentrated").isInstanceOf[Modifier] &&
      getModifier("Concentrated").name == "Concentrated")
  }
}
