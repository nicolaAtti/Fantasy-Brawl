package game

import messaging.RoundManager
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var id: Int = 0
  var turns: List[Character] = List()

  def startRound(): Unit = {
    RoundManager.startRoundRequest(Battle.playerId,
                                   Battle.teams.filter(char => char.owner.get == Battle.playerId).map(character => character.characterName -> character.speed).toMap,
                                   id + 1)
  }

  def setupTurns(turnInformation: List[((String, String), Int)], round: Int): Unit = {
    id = round
    turns = turnInformation.map {
      case ((playerName,characterName), _) =>
          Battle.teams.find(char => char.characterName == characterName && char.owner.get == playerName).get
    }
    if (id == 1)
      ApplicationView changeView BATTLE
  }
  def endRound(): Unit = {}
}
