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
  import alice.tuprolog.NoMoreSolutionException
  val charactersList: ListBuffer[Character] = ListBuffer()
  try {
    getAllCharacters().foreach(v => charactersList += v)
  } catch {
    case ex: NoMoreSolutionException => Unit
  }
  test("Extract all characters from prolog should return all the characters") {
    assert(
      charactersList
        .map(character => character.characterName)
        .toList == charList)
  }

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
