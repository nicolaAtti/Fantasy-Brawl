package game

import controller.BattleController
import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turns: List[(String, Character)] = List()

  def startRound(): Unit = {
    RoundManager.startRoundRequest(Battle.playerName,
                                   Battle.playerTeam.map(character => character._1 -> character._2.speed),
                                   id + 1)
  }

  def endRound(): Unit = {
    startRound()
  }

  def setupTurns(turnInformation: List[((String, String), Int)], round: Int): Unit = {
    id = round
    turns = turnInformation.map {
      case ((playerName, characterName), _) =>
        if (playerName == Battle.playerName)
          (playerName, Battle.playerTeam(characterName))
        else
          (playerName, Battle.opponentTeam(characterName))
    }
    startTurn()
    if (id == 1)
      ApplicationView changeView BATTLE
  }

  def startTurn(): Unit = {
    BattleController.setActiveCharacter(turns.head._2)
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
