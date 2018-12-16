package game

import controller.BattleController
import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turns: List[Character] = List()
  val PhysicalAttackRepresentation: String = "Physical Attack"

  def startNewRound(): Unit = {
    RoundManager.startRoundRequest(
      Battle.playerId,
      Battle.teams
        .filter(character => character.owner.get == Battle.playerId && character.isAlive)
        .map(character => character.characterName -> character.speed)
        .toMap,
      Battle.opponentId,
      Battle.id,
      id + 1
    )
  }

  def endRound(): Unit = {
    if (!Battle.teams.exists(character => character.owner.get == Battle.playerId && character.isAlive))
      Battle.end(Battle.opponentId)
    else if (!Battle.teams.exists(character => character.owner.get == Battle.opponentId && character.isAlive))
      Battle.end(Battle.playerId)
    else
      startNewRound()
  }

  def setupTurns(turnInformation: List[(String, String)], round: Int): Unit = {
    id = round
    turns = turnInformation.map {
      case (playerName, characterName) =>
        Battle.teams.find(char => char.characterName == characterName && char.owner.get == playerName).get
    }
    if (id == 1)
      ApplicationView changeView BATTLE
    startTurn()
  }

  def startTurn(): Unit = {
    val activeCharacter = turns.head
    if (activeCharacter.isAlive) {
      activeCharacter.status = Status.afterTurnStart(activeCharacter.status)
      BattleController.setActiveCharacter(activeCharacter)
    } else {
      endTurn()
    }
  }

  def endTurn(): Unit = {
    turns = turns.tail
    BattleController.updateStatus()
    if (turns.nonEmpty)
      startTurn()
    else
      endRound()
  }

  def actCalculation(moveName: String, targets: List[Character]): Unit ={
    val activeCharacter = turns.head
    var newStatuses = _
    if(moveName == PhysicalAttackRepresentation)
      newStatuses = Move.makeMove(PhysicalAttack, activeCharacter, targets.toSet)
    else
      newStatuses = Move.makeMove(activeCharacter.specialMoves(moveName), activeCharacter, targets.toSet)

    // TODO update status

    endTurn()
  }
}
