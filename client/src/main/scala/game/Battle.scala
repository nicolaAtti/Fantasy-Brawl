package game

import controller.BattleController
import javafx.application.Platform
import messaging.BattleManager
import model._
import utilities.ScalaProlog._

object Battle extends App {
  var teams: Set[Character] = Set()
  var id: String = _
  var playerId: String = _
  var opponentId: String = _
  var opponentQueue: String = _

  import BattleHelper._

  def start(player: (String, Set[String]),
            opponent: (String, Set[String]),
            battleQueue: String,
            battleId: String): Unit = {
    id = battleId
    playerId = player._1
    opponentId = opponent._1
    opponentQueue = battleQueue
    BattleManager.start()
    teams = setupTeam(player, opponent)
    Round.startNewRound()
  }

  def end(winner: String): Unit = {
    Platform runLater (() => {
      BattleController.settingWinner(winner)
    })
  }

  private object BattleHelper {

    def setupTeam(player: (String, Set[String]), opponent: (String, Set[String])): Set[Character] = {
      player._2.map(characterName => getCharacter(characterName, Some(player._1))) ++
        opponent._2.map(characterName => getCharacter(characterName, Some(opponent._1)))
    }
  }
}
