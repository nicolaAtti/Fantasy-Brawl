package utilities

import scala.io.Source
import alice.tuprolog._
import model._

object ScalaProlog {
  import ScalaPrologHelper._
  val engine = new Prolog

  implicit def termToString(term: Term): String = term.toString.replace("'", "")
  implicit def termToInt(term: Term): scala.Int = term.toString.toInt
  implicit def termToList(term: Term): List[String] =
    term.toString.replace("'", "").replace("[", "").replace("]", "").split(",").filter(s => s != "").toList

  def getCharacter(characterName: String): Character = {
    setNewTheory(characterContents)
    val solveInfo = engine.solve(
      "character('" + characterName + "',CharacterClass,Strength,Agility,Spirit,Intelligence,Resistance,SpecialMoves).")
    Character(
      characterClass = extractString(solveInfo, "CharacterClass"),
      characterName = characterName,
      statistics = Statistics(
        extractInt(solveInfo, "Strength"),
        extractInt(solveInfo, "Agility"),
        extractInt(solveInfo, "Spirit"),
        extractInt(solveInfo, "Intelligence"),
        extractInt(solveInfo, "Resistance")
      ),
      specialMoves = extractList(solveInfo, "SpecialMoves").map(moveName => moveName -> getSpecialMove(moveName)).toMap
    )
  }

  def getSpecialMove(moveName: String): SpecialMove = {
    setNewTheory(moveContents)
    val solveInfo = engine.solve(
      "spec_move('" + moveName + "',MoveEffectType,MoveType,BaseValue,AddModifiers,AddAlterations,RemoveAlterations,ManaCost,MaxTargets).")
    SpecialMove(
      name = moveName,
      moveEffectType = extractString(solveInfo, "MoveEffectType"),
      moveType = MoveType(extractString(solveInfo, "MoveType")),
      baseValue = extractInt(solveInfo, "BaseValue"),
      addModifiers =
        extractList(solveInfo, "AddModifiers").map(modifierName => modifierName -> getModifier(modifierName)).toMap,
      addAlterations = extractList(solveInfo, "AddAlterations")
        .map(alteration => Alteration(alteration) -> Alteration(alteration).turnDuration)
        .toMap,
      removeAlterations = extractList(solveInfo, "RemoveAlterations").map(alteration => Alteration(alteration)).toSet,
      manaCost = extractInt(solveInfo, "ManaCost"),
      maxTargets = extractInt(solveInfo, "MaxTargets")
    )
  }

  def getModifier(modifierName: String): Modifier = {
    setNewTheory(moveContents)
    val solveInfo = engine.solve("modifier('" + modifierName + "',SubStatistic,RoundsDuration,Delta).")
    Modifier(modifierName,
             SubStatistic(extractString(solveInfo, "SubStatistic")),
             extractInt(solveInfo, "Delta"),
             extractInt(solveInfo, "RoundsDuration"))
  }

  private object ScalaPrologHelper {
    val characterContents =
      Source.fromResource("model/PrologCharacters.pl").getLines.reduce((line1, line2) => line1 + "\n" + line2)
    val moveContents = Source.fromResource("model/PrologMoves.pl").getLines.reduce((line1, line2) => line1 + "\n" + line2)

    def setNewTheory(clauses: String*): Unit =
      engine.setTheory(new Theory(clauses mkString " "))

    def extractInt(solveInfo: SolveInfo, value: String): scala.Int = {
      solveInfo.getVarValue(value)
    }

    def extractString(solveInfo: SolveInfo, value: String): String = {
      solveInfo.getVarValue(value)
    }

    def extractList(solveInfo: SolveInfo, value: String): List[String] = {
      solveInfo.getVarValue(value)
    }
  }
}
