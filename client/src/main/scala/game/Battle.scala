package game

import model._
import utilities.ScalaProlog._

object Battle extends App {
  var teams: Set[Character] = Set()
  var id: String = _
  var playerId: String = _
  var opponentId: String = _
  var playerQueue: String = communication.Queues.BattleQueue
  var opponentQueue: String = _

  import BattleHelper._

  def start(player: (String, Set[String]), opponent: (String, Set[String]), battleId: String): Unit = {
    playerId = player._1
    opponentId = opponent._1
    teams = setupTeam(player, opponent)
    id = battleId
    Round.startNewRound()
  }

  def end(winner: String): Unit = {
    // TODO set the winner
  }

  private object BattleHelper {

    def setupTeam(player: (String, Set[String]), opponent: (String, Set[String])): Set[Character] = {
      player._2.map(characterName => getCharacter(characterName, Some(player._1))) ++
        opponent._2.map(characterName => getCharacter(characterName, Some(opponent._1)))
    }
  }
}
