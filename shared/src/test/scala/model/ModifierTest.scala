package model

import org.scalatest.FunSuite

/** Test for all the modifier features
  *
  * @author Nicola Atti
  */
class ModifierTest extends FunSuite {
  val testModifier = Modifier("Concentrated",SubStatistic("MagicalDamage"),30,1)
  val wrongSubStatistic = SubStatistic("PhysicalDamage")
  val rightSubStatistic = SubStatistic("MagicalDamage")

  test("A new Modifier should have the right name"){
    assert(testModifier.name equals "Concentrated")
  }
  test("A new Modifier should affect the correct SubStatistic"){
    assert(testModifier.affectsSubStatistic != wrongSubStatistic &&
    testModifier.affectsSubStatistic == rightSubStatistic)
  }
  test("A new Modifier should have the correct duration"){
    assert(testModifier.remainingRounds == 1)
  }
  test("A new Modifier should have the right delta value"){
    assert(testModifier.delta == 30)
  }


}
