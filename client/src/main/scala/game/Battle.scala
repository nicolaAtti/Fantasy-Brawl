package game

import model._
import utilities.ScalaProlog._

object Battle extends App {
  var teams: Set[Character] = Set()
  var id: String = _
  var playerId: String = _
  var opponentId: String = _

  import BattleHelper._

  def start(player: (String, Seq[String]), opponent: (String, Seq[String]), battleId: String): Unit = {
    playerId = player._1
    opponentId = opponent._1
    teams = setupTeam(player, opponent)
    teams.foreach(println(_))
    id = battleId
    Round.startRound()
  }

  private object BattleHelper {

    def setupTeam(player: (String, Seq[String]), opponent: (String, Seq[String])): Set[Character] = {
      (player._2.map(characterName => getCharacter(characterName, Some(player._1))) ++
        opponent._2.map(characterName => getCharacter(characterName, Some(opponent._1)))).toSet
    }
  }
}
