package game

import controller.BattleController
import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turns: List[Character] = List()

  def startRound(): Unit = {
    RoundManager.startRoundRequest(
      Battle.playerId,
      Battle.teams
        .filter(char => char.owner.get == Battle.playerId)
        .map(character => character.characterName -> character.speed)
        .toMap,
      Battle.opponentId,
      Battle.id,
      id + 1
    )
  }

  def endRound(): Unit = {
    startRound()
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
    BattleController.setActiveCharacter(turns.head)
    turns = turns.tail
    // ----------------------------------- applicare afflizioni
  }

  def endTurn(): Unit = {
    if (turns.nonEmpty)
      startTurn()
    else
      endRound()
  }
}
