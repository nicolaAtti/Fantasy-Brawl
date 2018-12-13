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
    RoundManager.startRoundRequest(Battle.playerId,
                                   Battle.teams
                                     .filter(char => char.owner.get == Battle.playerId)
                                     .map(character => character.characterName -> character.speed)
                                     .toMap,
                                   id + 1)
  }

  def endRound(): Unit = {
    startRound()
  }

  def setupTurns(turnInformation: List[((String, String), Int)], round: Int): Unit = {
    id = round
    turns = turnInformation.map {
      case ((playerName, characterName), _) =>
        Battle.teams.find(char => char.characterName == characterName && char.owner.get == playerName).get
    }
    startTurn()
    if (id == 1)
      ApplicationView changeView BATTLE
  }

  def startTurn(): Unit = {
    BattleController.setActiveCharacter(turns.head)
    turns = turns.tail
    // ----------------------------------- applicare afflizioni
    //println(id)
    //turn.foreach{ case (playerName, character) => println(playerName + " - " + character.characterName + " : " + character.speed)}
  }

  def endTurn(): Unit = {
    if (turns.nonEmpty)
      startTurn()
    else
      endRound()
  }
}
