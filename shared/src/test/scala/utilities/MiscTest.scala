package utilities

import org.scalatest.FunSuite
import Misc._

class MiscTest extends FunSuite {

  test("The clamped function should return a value between the given max and min values") {
    assert(
      clamped(10, 0, 8) == 8 &&
        clamped(5, 10, 15) == 10)
  }

  test("The roundDown function should round down a double value and convert it to an int") {
    assert(roundDown(32.4) == 32)
  }

  test("The roundUp function should round up a double value and convert it to an int") {
    assert(roundUp(32.5) == 33)
  }

  test("The isEven function should return true only for even numbers") {
    assert(
      isEven(2) && isEven(8) && isEven(58) &&
        !isEven(7) && !isEven(99) && !isEven(5555))
  }

  test("The isOdd function should return true only for odd numbers") {
    assert(isOdd(3) && isOdd(11) && isOdd(119) &&
      !isOdd(14) && !isOdd(100) && !isOdd(3332))
  }

}
