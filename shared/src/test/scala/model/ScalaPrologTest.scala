package model

import org.scalatest.FunSuite
import utilities.ScalaProlog._

class ScalaPrologTest extends FunSuite {
  private val charList: List[String] =
    List("Jacob", "Annabelle", "Albert", "Lidya", "Cassandra", "Noah", "Fernando", "Nora")
  private val jacobMoveList: List[String] =
    List("Physical Attack", "Skullcrack", "Sismic Slam", "Berserker Rage", "Second Wind")
  private val spellList: List[String] = List("Lay On Hands",
                                             "Censure",
                                             "Bolster Faith",
                                             "Preparation",
                                             "Second Wind",
                                             "Berserker Rage",
                                             "Freezing Wind",
                                             "Hypnosis",
                                             "Concentration",
                                             "Flamestrike")

  import scala.collection.mutable.ListBuffer
  import alice.tuprolog.SolveInfo
  import alice.tuprolog.NoMoreSolutionException
  val charactersList: ListBuffer[SolveInfo] = ListBuffer()
  try {
    getAllCharacters().foreach(v => charactersList += v)
  } catch {
    case ex: NoMoreSolutionException => Unit
  }
  test("Get all facts and extract a String from each of them") {
    assert(
      charactersList
        .map(character => extractString(character, "Name"))
        .toList == charList)
  }

  test("Extract an Int from a prolog fact of a move") {
    assert(extractInt(getMove("Skullcrack"), "BaseValue") == 30)
  }
  test("Extract an Int from a prolog fact of a character") {
    assert(extractInt(getCharacter("Jacob"), "Strength") == 51)
  }
  test("Extract a List from a prolog fact") {
    assert(extractList(getCharacter("Jacob"), "MoveList") == jacobMoveList)
  }
  setNewTheory(moveContents)
  val movesList: ListBuffer[SolveInfo] = ListBuffer()
  try {
    solveN("move(MoveName,DamageType,Type,BaseValue,Mods,Affls,RemovedAfflictions,MPCost,NTargets).").foreach(v =>
      movesList += v)
  } catch {
    case ex: NoMoreSolutionException => Unit
  }

  val allMoves: List[String] = movesList
    .map(move => extractString(move, "MoveName"))
    .toList
  test("Get all possible results given a custom goal and extract a value") { //da rifare
    assert(
      spellList
        .map(moveName => allMoves contains moveName)
        .forall(_ == true))
  }

}
