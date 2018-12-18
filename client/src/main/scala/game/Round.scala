package game

import communication.StatusUpdateMessage.CharacterKey
import controller.BattleController
import javafx.application.Platform
import messaging.{BattleManager, RoundManager}
import model._
import view.ApplicationView
import view.ViewConfiguration.ViewSelector._

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
    turns.foreach(character => println(character.owner.get, character.characterName, character.speed))
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
    Platform runLater (() => {
      BattleController.resetTargets()
      BattleController.updateStatus()
    })
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
    var newStatuses: Map[CharacterKey, Status] = Map()
    if (moveName == PhysicalAttackRepresentation)
      newStatuses = Move.makeMove(PhysicalAttack, attacker, targets.toSet).map {
        case (character, status) => (character.owner.get, character.characterName) -> status
      } else
      newStatuses = Move.makeMove(attacker.specialMoves(moveName), attacker, targets.toSet).map {
        case (character, status) => (character.owner.get, character.characterName) -> status
      }
    updateTeamsStatuses(newStatuses)
    BattleManager.updateOpponentStatus(
      (attacker.owner.get, attacker.characterName),
      moveName,
      targets
        .map(character => (character.owner.get, character.characterName))
        .toSet,
      newStatuses,
      Round.roundId
    )
    Platform runLater (() => {
      BattleController.displayMoveEffect(attacker, moveName, targets.toSet)
    })
    endTurn()
  }

  def updateTeamsStatuses(newStatuses: Map[CharacterKey, Status]): Unit = {
    Battle.teams.foreach(character => {
      if (newStatuses.contains((character.owner.get, character.characterName))) {
        character.status = newStatuses((character.owner.get, character.characterName))
      }
    })
  }
}
