package utilities

import alice.tuprolog._
import scala.io.Source


object ScalaProlog {
  val engine = new Prolog

  implicit def termToString(term: Term): String = term.toString.replace("'","")
  implicit def termToInt(term: Term): scala.Int = term.toString.toInt
  implicit def termToList(term: Term): List[String] = term.toString.replace("'","").replace("[","").replace("]","").split(",").toList

  val characterContents = Source.fromFile("C:\\Users\\UTENTE\\IdeaProjects\\pps-17-fb\\client\\src\\main\\resources\\model\\PrologCharacters.pl").getLines.reduce((line1, line2) => line1 + "\n" + line2)
  val moveContents = Source.fromFile("C:\\Users\\UTENTE\\IdeaProjects\\pps-17-fb\\client\\src\\main\\resources\\model\\PrologMoves.pl").getLines.reduce((line1,line2) => line1 + "\n" + line2)

  def setNewTheory(clauses: String*): Unit =
    engine.setTheory(new Theory(clauses mkString " "))

  def extractInt(solveInfo: SolveInfo,value: String): scala.Int ={
    solveInfo.getVarValue(value)
  }
  def extractString(solveInfo: SolveInfo,value: String): String = {
    solveInfo.getVarValue(value)
  }

  def extractList(solveInfo: SolveInfo,value: String): List[String] = {
    solveInfo.getVarValue(value)
  }

  def solveN( query: String): Stream[SolveInfo] = {
    engine.solve(query) #:: Stream.continually(engine.solveNext())
  }

  def getAllCharacters(): Stream[SolveInfo] = {
    setNewTheory(characterContents)
    solveN("character(Name,Class,Strength,Agility,Spirit,Intelligence,Resistence,MoveList).")
  }

  def getCharacter(characterName: String): SolveInfo = {
    setNewTheory(characterContents)
    engine.solve("character('"+characterName+"',Class,Strength,Agility,Spirit,Intelligence,Resistence,MoveList).")
  }

  def getMove(moveName: String): SolveInfo = {
    setNewTheory(moveContents)
    engine.solve("spec_move('"+moveName+"',OffStat,DefStat,Type,BaseValue,MPCost,Mods,Affls,NTargets).")
  }
}
