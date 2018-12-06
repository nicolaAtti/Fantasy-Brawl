package utilities

import scala.io.Source
import alice.tuprolog._

object ScalaProlog {
  val engine = new Prolog

  implicit def termToString(term: Term): String = term.toString.replace("'", "")
  implicit def termToInt(term: Term): scala.Int = term.toString.toInt
  implicit def termToList(term: Term): List[String] =
    term.toString.replace("'", "").replace("[", "").replace("]", "").split(",").toList

  implicit def solutionTupleToModifierValues(solution: (String, SolveInfo)): (String, String, scala.Int, scala.Int) =
    (solution._1,
     extractString(solution._2, "Statistic"),
     extractInt(solution._2, "Value"),
     extractInt(solution._2, "Duration"))

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

  def solveN(query: String): Stream[SolveInfo] = {
    engine.solve(query) #:: Stream.continually(engine.solveNext())
  }

  def getAllCharacters(): Stream[SolveInfo] = {
    setNewTheory(characterContents)
    solveN("character(Name,Class,Strength,Agility,Spirit,Intelligence,Resistence,MoveList).")
  }

  def getCharacter(characterName: String): SolveInfo = {
    setNewTheory(characterContents)
    engine.solve("character('" + characterName + "',Class,Strength,Agility,Spirit,Intelligence,Resistence,MoveList).")
  }

  def getMove(moveName: String): SolveInfo = {
    setNewTheory(moveContents)
    engine.solve("move('" + moveName + "',DamageType,Type,BaseValue,Mods,Affls,RemovedAfflictions,MPCost,NTargets).")
  }

  def getModifier(modifierName: String): (String, SolveInfo) = {
    setNewTheory(moveContents)
    (modifierName, engine.solve("modifier('" + modifierName + "',Statistic,Duration,Value)."))
  }
}
