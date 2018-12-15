package utilities

import org.scalatest.FunSuite

class MiscTest extends FunSuite {

  test("The clamped function should return a value between the given max and min values") {
    assert(
      Misc.clamped(10, 0, 8) == 8 &&
        Misc.clamped(5, 10, 15) == 10)
  }

  test("The roundDown function should round down a double value and convert it to an int") {
    assert(Misc.roundDown(32.4) == 32)
  }

  test("The roundUp function should round up a double value and convert it to an int") {
    assert(Misc.roundUp(32.5) == 33)
  }

}
