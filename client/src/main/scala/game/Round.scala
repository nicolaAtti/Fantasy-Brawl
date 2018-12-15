package game

import controller.BattleController
import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turns: List[Character] = List()

  def startNewRound(): Unit = {
    RoundManager.startRoundRequest(
      Battle.playerId,
      Battle.teams
        .filter(character => character.owner.get == Battle.playerId && character.status.healthPoints > 0)
        .map(character => character.characterName -> character.speed)
        .toMap,
      Battle.opponentId,
      Battle.id,
      id + 1
    )
  }

  def endRound(): Unit = {
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
    turns = turns.tail
    val newStatus = Status.afterTurnStart(activeCharacter.status)
    if(activeCharacter.status.healthPoints > 0 && newStatus.healthPoints > 0){
      activeCharacter.status = newStatus
      BattleController.setActiveCharacter(activeCharacter)
    } else {
      //BattleController.characterDied(activeCharacter)
      endTurn()
    }
  }

  def endTurn(): Unit = {
    if (turns.nonEmpty)
      startTurn()
    else
      endRound()
  }
}
