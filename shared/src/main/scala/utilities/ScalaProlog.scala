package utilities

import scala.io.Source
import alice.tuprolog._
import model._

object ScalaProlog {
  import ScalaPrologHelper._

  implicit def termToString(term: Term): String = term.toString.replace("'", "")
  implicit def termToInt(term: Term): scala.Int = term.toString.toInt
  implicit def termToList(term: Term): List[String] =
    term.toString.replace("'", "").replace("[", "").replace("]", "").split(",").filter(s => s != "").toList

  /** Executes a Prolog query to obtain a specific character.
    *
    * @param characterName the character's name
    * @return the Character object
    */
  def getCharacter(characterName: String, owner: Option[String]): Character = {
    setNewTheory(characterContents)
    val solveInfo = engine.solve(
      "character('" + characterName + "',CharacterClass,Strength,Agility,Spirit,Intelligence,Resistance,SpecialMoves).")
    Character(
      role = extractString(solveInfo, "CharacterClass"),
      owner = owner,
      name = characterName,
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

  /** Creates a character with no owner
    *
    * @param characterName the character's name
    * @return a Character with owner set to "None"
    */
  def getCharacter(characterName: String): Character = getCharacter(characterName, None)

  /** Executes a Prolog query to obtain a specific special move.
    *
    * @param moveName the name of the move
    * @return the SpecialMove object
    */
  def getSpecialMove(moveName: String): SpecialMove = {
    setNewTheory(moveContents)
    val solveInfo = engine.solve(
      "spec_move('" + moveName + "',MoveEffectStrategy,MoveType,BaseValue,AddModifiers,AddAlterations,RemoveAlterations,ManaCost,MaxTargets).")
    SpecialMove(
      name = moveName,
      moveType = MoveType(extractString(solveInfo, "MoveType")),
      moveEffect = MoveEffectStrategies(
        moveEffectStrategyCode = extractString(solveInfo, "MoveEffectStrategy"),
        moveType = MoveType(extractString(solveInfo, "MoveType")),
        baseValue = extractInt(solveInfo, "BaseValue")
      )(
        addModifiers =
          extractList(solveInfo, "AddModifiers").map(modifierName => modifierName -> getModifier(modifierName)).toMap,
        addAlterations = extractList(solveInfo, "AddAlterations")
          .map(alteration => Alteration(alteration) -> Alteration(alteration).roundsDuration)
          .toMap,
        removeAlterations = extractList(solveInfo, "RemoveAlterations").map(alteration => Alteration(alteration)).toSet,
      ),
      manaCost = extractInt(solveInfo, "ManaCost"),
      maxTargets = extractInt(solveInfo, "MaxTargets")
    )
  }

  /** Executes a Prolog query to obtain a specific modifier.
    *
    * @param modifierName the modifier's name
    * @return the Modifier object
    */
  def getModifier(modifierName: String): Modifier = {
    setNewTheory(moveContents)
    val solveInfo = engine.solve("modifier('" + modifierName + "',SubStatistic,RoundsDuration,Delta).")
    Modifier(modifierName,
             SubStatistic(extractString(solveInfo, "SubStatistic")),
             extractInt(solveInfo, "Delta"),
             extractInt(solveInfo, "RoundsDuration"))
  }

  private object ScalaPrologHelper {

    val engine = new Prolog

    val characterContents: String =
      Source.fromResource("model/PrologCharacters.pl").getLines.reduce((line1, line2) => line1 + "\n" + line2)

    val moveContents: String =
      Source.fromResource("model/PrologMoves.pl").getLines.reduce((line1, line2) => line1 + "\n" + line2)

    def setNewTheory(clauses: String*): Unit =
      engine.setTheory(new Theory(clauses mkString " "))

    /** Extracts an Int value from a Prolog solution
      *
      * @param solveInfo the solution from a Prolog query
      * @param value the name of the value to extract
      * @return the extracted Int value
      */
    def extractInt(solveInfo: SolveInfo, value: String): scala.Int = {
      solveInfo.getVarValue(value)
    }

    /** Extracts a String from a Prolog solution
      *
      * @param solveInfo the solution from a Prolog query
      * @param value the name of the value to extract
      * @return the extracted String
      */
    def extractString(solveInfo: SolveInfo, value: String): String = {
      solveInfo.getVarValue(value)
    }

    /** Builds a Scala list of strings from a Prolog list
      *
      * @param solveInfo the solution from a Prolog query
      * @param value the name of the value to extract
      * @return the built List of string
      */
    def extractList(solveInfo: SolveInfo, value: String): List[String] = {
      solveInfo.getVarValue(value)
    }
  }

}
