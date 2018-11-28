package model
import alice.tuprolog.{NoMoreSolutionException, Term}
import org.scalatest.FunSuite
import utilities.ScalaProlog._

class ScalaPrologTest extends FunSuite {
  private val charList: List[String] = List("Jacob","Annabelle","Albert","Lidya")
  private val jacobMoveList: List[String] = List("Skullcrack","Sismic Slam","Berserker Rage","Second Wind")

  test("Asking for all character names should return all of them") {
    try {
      getAllCharacters() foreach (sol => assert(charList.contains(extractString(sol,"Name"))))
    } catch {
      case ss: NoMoreSolutionException =>
    }
  }

  test("A particular value of a term should be returned when asked"){
    try {
      assert(extractInt(getMove("Skullcrack"),"BaseValue") == 30)
    } catch {
      case ss: NoMoreSolutionException =>
    }
  }
  test("Asking for a list should return a scala list"){
    try {
      assert(extractList(getCharacter("Jacob"),"MoveList") == jacobMoveList)
    } catch {
      case ss: NoMoreSolutionException =>
    }
  }

}
