package game

import communication.StatusUpdateMessage._
import controller.BattleController
import javafx.application.Platform
import messaging.{BattleManager, RoundManager}
import model._
import view.ApplicationView
import view.ViewConfiguration.ViewSelector._
import ActSelector._

/** Provides the functionality to manage the initialization and termination of each
  * round.
  *
  * @author Daniele Schiavi
  */
object Round {
  var roundId: Int = _
  var turns: List[Character] = List()
  val PhysicalAttackRepresentation: String = "Physical Attack"

  /** Start a new round. */
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

  /** Given an ordered list of characters and the starting round, setts the round
    * information and starts the first turn.
    *
    * @param turnInformation the ordered list of characters
    * @param round the starting round
    */
  def setupTurns(turnInformation: List[(String, String)], round: Int): Unit = {
    roundId = round
    turns = turnInformation.map {
      case (playerName, characterName) =>
        Battle.teams.find(char => char.characterName == characterName && char.owner.get == playerName).get
    }
    if (roundId == 1)
      ApplicationView changeView BATTLE
    Platform runLater (() => {
      BattleController.roundCounter.setText(roundId.toString)
    })
    startTurn()
  }

  /** Starts a new turn. */
  def startTurn(): Unit = {
    val activeCharacter = turns.head
    if (activeCharacter.isAlive) {
      activeCharacter.status = Status.afterTurnStart(activeCharacter.status)
      if (activeCharacter.owner.get == Battle.playerId && activeCharacter.isIncapacitated) {
        BattleManager.skipTurn((activeCharacter.owner.get, activeCharacter.characterName), roundId)
        Platform runLater (() => BattleController.displayMoveEffect(activeCharacter, "", Set(), ActSelector.SKIP))
        endTurn()
      } else {
        Platform runLater (() => {
          BattleController.updateStatus()
          BattleController.setActiveCharacter(activeCharacter)
        })
      }
    } else {
      endTurn()
    }
  }

  /** Ends the current turn. */
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

  /** Given an attacker, his move and a list of targets, applies the move and updates
    * the state of the battle and the GUI.
    *
    * @param attacker the character that makes the move
    * @param moveName the move name
    * @param targets the list of characters affected by the move
    */
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
      BattleController.displayMoveEffect(attacker, moveName, targets.toSet, UPDATE)
    })
    endTurn()
  }

  /** Given a map of characters and their new statuses, updates the current status of
    * each character.
    *
    * @param newStatuses the map that couples each character with his new status
    *                    (if changed)
    */
  def updateTeamsStatuses(newStatuses: Map[CharacterKey, Status]): Unit = {
    Battle.teams.foreach(character => {
      if (newStatuses.contains((character.owner.get, character.characterName))) {
        character.status = newStatuses((character.owner.get, character.characterName))
      }
    })
  }
}
