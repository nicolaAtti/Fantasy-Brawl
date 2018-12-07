package model

import alice.tuprolog.NoMoreSolutionException
import org.scalatest.FunSuite
import utilities.ScalaProlog._

class ScalaPrologTest extends FunSuite {
  private val charList: List[String] = List("Jacob","Annabelle","Albert","Lidya")
  private val jacobMoveList: List[String] = List("Skullcrack","Sismic Slam","Berserker Rage","Second Wind")
  private val spellList: List[String] = List("Lay On Hands","Censure","Bolster Faith","Preparation","Second Wind","Berserker Rage","Freezing Wind","Hypnosis","Concentration","Flamestrike")

  test("Get all facts and extract a String from each of them") {
    try {
      getAllCharacters() foreach (sol => assert(charList.contains(extractString(sol,"Name"))))
    } catch {
      case ex: NoMoreSolutionException =>
    }
  }

  test("Extract an Int from a prolog fact"){
    try {
      assert(extractInt(getSpecialMove("Skullcrack"),"BaseValue") == 30)
      assert(extractInt(getCharacter("Jacob"),"Strength") == 51)
    } catch {
      case ex: NoMoreSolutionException =>
    }
  }
  test("Extract a List from a prolog fact"){
    try {
      assert(extractList(getCharacter("Jacob"),"MoveList") == jacobMoveList)
    } catch {
      case ex: NoMoreSolutionException =>
    }
  }
  test("Get all possible results given a custom goal and extract a value"){
    setNewTheory(moveContents)
    try {
      solveN("spec_move(MoveName,'Spell',BaseDamage,ManaCost,ModifiersList,AfflictionsList,NOfTargets).") foreach(sol => assert(spellList.contains(extractString(sol,"MoveName"))))
    }catch{
      case ex: NoMoreSolutionException =>
    }
  }

}
