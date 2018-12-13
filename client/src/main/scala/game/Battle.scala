package game

import model._
import utilities.ScalaProlog._

object Battle extends App {
  var playerName: String = _
  var opponentName: String = _
  var playerTeam: Map[String, Character] = Map()
  var opponentTeam: Map[String, Character] = Map()
  var id: String = _

  import BattleHelper._

  def start(player: (String, Seq[String]), opponent: (String, Seq[String]), battleId: String): Unit = {
    playerName = player._1
    opponentName = opponent._1
    playerTeam = setupTeam(player._2)
    opponentTeam = setupTeam(opponent._2)
    id = battleId
    Round.startRound()
  }

  private object BattleHelper {

    def setupTeam(team: Seq[String]): Map[String, Character] = {
      team
        .map(characterName => characterName -> getCharacter(characterName))
        .toMap
    }
  }
}
