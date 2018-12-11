package model

import org.scalatest.FunSuite
import utilities.ScalaProlog._

class ScalaPrologTest extends FunSuite {
  test("Extract a character from prolog should return a character") {
    assert(getCharacter("Jacob").isInstanceOf[Character])
  }
  test("Extract a special move from prolog should return a special move") {
    assert(getSpecialMove("Antidote").isInstanceOf[SpecialMove])
  }
  test("Extract a modifier from prolog should return a modifier") {
    assert(getModifier("Concentrated").isInstanceOf[Modifier])
  }
}
