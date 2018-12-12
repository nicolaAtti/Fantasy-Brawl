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

  def setupTurns(turnInformation: List[((String, String), Int)], round: Int): Unit = {
    id = round
    turn = turnInformation.map {
      case (key, speed) =>
        if (key._1 == Battle.playerName)
          (key._1, Battle.playerTeam(key._2))
        else
          (key._1, Battle.opponentTeam(key._2))
    }
    if (id == 1)
      ApplicationView changeView BATTLE
  }
  def endRound(): Unit = {}
}
