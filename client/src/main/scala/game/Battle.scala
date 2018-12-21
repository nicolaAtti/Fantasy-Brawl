package game

import communication.matchmaking.PlayerInfo
import controller.BattleController
import javafx.application.Platform
import model._
import utilities.ScalaProlog._

/** Provides the functionality to manage the initialization and termination of each
  * battle.
  *
  * @author Daniele Schiavi
  */
object Battle extends App {
  var teams: Set[Character] = Set()
  var id: String = _
  var playerId: String = _
  var opponentId: String = _
  var opponentQueue: String = _

  import BattleHelper._

  /** Starts a new battle between the player and the opponent given.
    *
    * @param player information about the actual player
    * @param opponentInfo information about the opponent player
    * @param battleId the battle identifier
    */
  def start(player: (String, Set[String]), opponentInfo: PlayerInfo, battleId: String): Unit = {
    id = battleId
    playerId = player._1
    opponentId = opponentInfo.name
    opponentQueue = opponentInfo.battleQueue
    teams = setupTeam(player, (opponentInfo.name, opponentInfo.teamNames))
    Round.roundId = 0
    Round.startNewRound()
  }

  /** Terminates the battle displaying the player that won.
    *
    * @param winner the name of the winner
    */
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
