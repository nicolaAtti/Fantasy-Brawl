package game

import controller.BattleController
import javafx.application.Platform
import messaging.{BattleManager, RoundManager}
import model.Move.NewStatuses
import model._
import view.ApplicationView
import view.ViewConfiguration.viewSelector._

object Round {
  var roundId: Int = 0
  var turns: List[Character] = List()
  val PhysicalAttackRepresentation: String = "Physical Attack"

  def startNewRound(): Unit = {
    RoundManager.startRoundRequest(
      Battle.playerId,
      Battle.teams
        .filter(character => character.owner.get == Battle.playerId && character.isAlive)
        .map(character => character.characterName -> character.speed)
        .toMap,
      Battle.opponentId,
      Battle.id,
      roundId + 1
    )
  }

  def setupTurns(turnInformation: List[(String, String)], round: Int): Unit = {
    roundId = round
    turns = turnInformation.map {
      case (playerName, characterName) =>
        Battle.teams.find(char => char.characterName == characterName && char.owner.get == playerName).get
    }
    if (roundId == 1)
      ApplicationView changeView BATTLE
    startTurn()
  }

  def startTurn(): Unit = {
    val activeCharacter = turns.head
    if (activeCharacter.isAlive) {
      activeCharacter.status = Status.afterTurnStart(activeCharacter.status)
      Platform runLater (() => {
        BattleController.setActiveCharacter(activeCharacter)
        BattleController.roundCounter.setText(roundId.toString)
      })
    } else {
      endTurn()
    }
  }

  def endTurn(): Unit = {
    BattleController.resetTargets()
    BattleController.updateStatus()
    val playerLost = !Battle.teams.exists(character => character.owner.get == Battle.playerId && character.isAlive)
    val opponentLost = !Battle.teams.exists(character => character.owner.get == Battle.opponentId && character.isAlive)
    (playerLost, opponentLost) match {
      case (true, true) if turns.head.owner.get == Battle.playerId   => Battle.end(Battle.playerId)
      case (true, true) if turns.head.owner.get == Battle.opponentId => Battle.end(Battle.opponentId)
      case (true, _)                                                 => Battle.end(Battle.opponentId)
      case (_, true)                                                 => Battle.end(Battle.playerId)
      case _ =>
        turns = turns.tail
        if (turns.nonEmpty)
          startTurn()
        else
          startNewRound()
    }
  }

  def actCalculation(attacker: Character, moveName: String, targets: List[Character]): Unit = {
    makeMoveAndUpdateTeamsStatuses(attacker, moveName, targets.toSet)
    BattleManager.updateOpponentStatus((attacker.owner.get, attacker.characterName),
                                       moveName,
                                       targets
                                         .map(character => (character.owner.get, character.characterName))
                                         .toSet,
                                       Round.roundId)
    endTurn()
  }

  def makeMoveAndUpdateTeamsStatuses(attacker: Character, moveName: String, targets: Set[Character]): Unit = {
    var newStatuses: Move.NewStatuses = Map()
    if (moveName == PhysicalAttackRepresentation)
      newStatuses = Move.makeMove(PhysicalAttack, attacker, targets)
    else
      newStatuses = Move.makeMove(attacker.specialMoves(moveName), attacker, targets)
    Battle.teams.foreach(character => if (newStatuses.contains(character)) character.status = newStatuses(character))
    // TODO BattleController.displayMoveEffect(attacker, moveName, targets)
  }
}
