package model

import org.scalatest.FunSuite
import Status._

class StatusTest extends FunSuite {

  val status = Status(100, 100, 100, 100, Map(), Map())
  test("The stunned alteration should inhibit all moves") {
    val newStatus = afterTurnStart(status)

  }
}

