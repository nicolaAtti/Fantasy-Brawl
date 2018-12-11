package game

import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turn: List[(String, Character)] = List()

  def startRound(): Unit = {
    RoundManager.startRoundRequest(Battle.playerName,
                                   Battle.playerTeam.map(character => character._1 -> character._2.speed),
                                   id + 1)
  }

  def setupTurns(turnInformation: (List[(String, String)], Int)): Unit = {
    id = turnInformation._2
    turn = turnInformation._1.map {
      case (owner, characterName) =>
        if (owner == Battle.playerName)
          (owner, Battle.playerTeam(characterName))
        else
          (owner, Battle.opponentTeam(characterName))
    }
    if (id == 1)
      ApplicationView changeView BATTLE
  }
  def endRound(): Unit = {}
}
