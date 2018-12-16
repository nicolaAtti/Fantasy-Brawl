package controller

import java.net.URL
import java.util.ResourceBundle

import game.{Battle, Round}
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.collections.{FXCollections, ObservableList}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.effect.DropShadow
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import model.{Character, Move}
import view.ApplicationView

import scala.collection.mutable.ListBuffer

//noinspection ScalaDocMissingParameterDescription,FieldFromDelayedInit
object BattleController extends Initializable with ViewController {

  import BattleControllerHelper._

  val controller: ViewController = this

  final val TargetedEffect: DropShadow = new DropShadow(0, 0, 0, Color.BLUE)
  final val ActivePlayerEffect: DropShadow = new DropShadow(0, 0, 0, Color.LIME)
  final val ActiveOpponentEffect: DropShadow = new DropShadow(0, 0, 0, Color.RED)

  @FXML var playerCharNames: VBox = _
  @FXML var playerHps: VBox = _
  @FXML var playerMps: VBox = _
  @FXML var playerAlterations: VBox = _

  @FXML var opponentCharNames: VBox = _
  @FXML var opponentHps: VBox = _
  @FXML var opponentMps: VBox = _
  @FXML var opponentAlterations: VBox = _

  @FXML var playerChar1Image: ImageView = _
  @FXML var playerChar2Image: ImageView = _
  @FXML var playerChar3Image: ImageView = _
  @FXML var playerChar4Image: ImageView = _

  @FXML var opponentChar1Image: ImageView = _
  @FXML var opponentChar2Image: ImageView = _
  @FXML var opponentChar3Image: ImageView = _
  @FXML var opponentChar4Image: ImageView = _

  @FXML var timerCounter: Label = _
  @FXML var roundCounter: Label = _

  @FXML var moveListView: ListView[String] = _

  @FXML var actButton: Button = _

  @FXML var winnerLabel: Label = _

  var playerImages: List[ImageView] = List()
  var opponentImages: List[ImageView] = List()

  var playerCharacterImages: Map[ImageView, Character] = Map()
  var opponentCharacterImages: Map[ImageView, Character] = Map()

  var targetImages: ListBuffer[ImageView] = ListBuffer()
  var targets: ListBuffer[Character] = ListBuffer()

  var moveList: ObservableList[String] = FXCollections.observableArrayList()
  var activeCharacter: Character = _
  var activeLabel: Label = _
  var activeImage: ImageView = _

  val timeline: Timeline = new Timeline()
  var timeSeconds: Int = config.MiscSettings.TurnDurationInSeconds

  /** Initializes the elements composing the Battle GUI
    *
    * @param location
    * @param resources
    */
  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    TargetedEffect.setHeight(30)
    TargetedEffect.setWidth(30)
    ActivePlayerEffect.setHeight(30)
    ActivePlayerEffect.setWidth(30)
    ActiveOpponentEffect.setHeight(30)
    ActiveOpponentEffect.setWidth(30)

    playerImages = List(playerChar1Image, playerChar2Image, playerChar3Image, playerChar4Image)
    opponentImages = List(opponentChar1Image, opponentChar2Image, opponentChar3Image, opponentChar4Image)
    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.getKeyFrames.add(
      new KeyFrame(
        Duration.seconds(1),
        (_: ActionEvent) => {
          timeSeconds = timeSeconds - 1
          timerCounter.setText(timeSeconds.toString)
          if (timeSeconds <= 0) {
            timeline.stop()
            Round.endTurn()
          }
        }
      ))
    setBattlefield()
    timeline.playFromStart()
  }

  /** Setups the GUI for both teams */
  def setBattlefield(): Unit = {
    setupTeams(Battle.teams)
  }

  def settingWinner(winner: String): Unit = {
    winnerLabel.setText("WINNER IS " + winner.toUpperCase)
    winnerLabel.setVisible(true)
  }

  /** Sets the player's team images and status description labels
    *
    * @param team  the team to setup
    */
  def setupTeams(team: Set[Character]): Unit = {
    playerCharacterImages = (playerImages zip team.filter(character => character.owner.get == Battle.playerId)).toMap
    opponentCharacterImages =
      (opponentImages zip team.filter(character => character.owner.get == Battle.opponentId)).toMap
    prepareImages()
    setupLabels()

  }

  /** Updates the status of each character in the battle,
    * showing it's current health and mana values compared with it' maximum
    * and by showing the various alterations that are active.
    * After that changes the images of dead characters.
    */
  def updateStatus(): Unit = {
    (playerCharacterImages.values zip playerHps.getChildren.toArray) foreach (couple =>
      couple._2
        .asInstanceOf[Label]
        .setText(couple._1.status.healthPoints + Separator + couple._1.status.maxHealthPoints))
    (playerCharacterImages.values zip playerMps.getChildren.toArray) foreach (couple =>
      couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + Separator + couple._1.status.maxManaPoints))
    (playerCharacterImages.values zip playerAlterations.getChildren.toArray) foreach (couple =>
      couple._2
        .asInstanceOf[Label]
        .setText(
          couple._1.status.alterations.keySet.map(alt => alt.acronym).foldRight("")(_ + Separator + _).dropRight(1)))

    (opponentCharacterImages.values zip opponentHps.getChildren.toArray) foreach (couple =>
      couple._2
        .asInstanceOf[Label]
        .setText(couple._1.status.healthPoints + Separator + couple._1.status.maxHealthPoints))
    (opponentCharacterImages.values zip opponentMps.getChildren.toArray) foreach (couple =>
      couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + Separator + couple._1.status.maxManaPoints))
    (opponentCharacterImages.values zip opponentAlterations.getChildren.toArray) foreach (couple =>
      couple._2
        .asInstanceOf[Label]
        .setText(
          couple._1.status.alterations.keySet.map(alt => alt.acronym).foldRight("")(_ + Separator + _).dropRight(1)))

    setDeadCharacters()
  }

  /** Obtains the current active character from the Battle and changes the corresponding label.
    *
    * The label is painted green to symbolize which character has now the turn, if the player owns
    * the character, the GUI will show his move list.
    */
  def setActiveCharacter(character: Character): Unit = {
    if (activeLabel != null) {
      activeLabel.setTextFill(Color.BLACK)
      activeImage.setEffect(null)
    }
    activeCharacter = character
    if (activeCharacter.owner.get == Battle.playerId) {
      activeLabel = playerCharNames.getChildren.toArray
        .filter(charName => charName.asInstanceOf[Label].getText equals activeCharacter.characterName)
        .head
        .asInstanceOf[Label]
      activeLabel.setTextFill(Color.GREEN)
      activeImage = playerCharacterImages.find(char => char._2 == activeCharacter).get._1
      activeImage.setEffect(ActivePlayerEffect)
      setupCharacterMoves(activeCharacter)
    } else {
      activeLabel = opponentCharNames.getChildren.toArray
        .filter(charName => charName.asInstanceOf[Label].getText equals activeCharacter.characterName)
        .head
        .asInstanceOf[Label]
      activeLabel.setTextFill(Color.RED)
      activeImage = opponentCharacterImages.find(char => char._2 == activeCharacter).get._1
      activeImage.setEffect(ActiveOpponentEffect)
    }
    newTurn()
  }

  /** Resets the turn timer */
  def newTurn(): Unit = {
    timeSeconds = config.MiscSettings.TurnDurationInSeconds
    timerCounter.setText(timeSeconds.toString)
    timeline.playFromStart()
  }

  private object BattleControllerHelper {
    val ImageExtension: String = ".png"
    val Separator: String = "/"
    val PhysicalAttackRepresentation: String = "Physical Attack"
    val MovesSeparator: String = "--"

    /** Assigns to each character it's battle image, orientation depends on the player  */
    def prepareImages(): Unit = {
      playerCharacterImages.foreach(charImage =>
        charImage._1.setImage(new Image("view/" + charImage._2.characterName + "1" + ImageExtension)))
      opponentCharacterImages.foreach(charImage =>
        charImage._1.setImage(new Image("view/" + charImage._2.characterName + "2" + ImageExtension)))
    }

    /** Writes all the team's labels to display the name of each character in the battle
      */
    def setupLabels(): Unit = {
      (playerCharacterImages.values zip playerCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.characterName))
      (opponentCharacterImages.values zip opponentCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.characterName))
      updateStatus()
    }

    /** Checks if the team members of the player are at zero health points,
      * and sets their image to a tombstone.
      */
    def setDeadCharacters(): Unit = {
      playerCharacterImages.foreach(couple =>
        if (couple._2.status.healthPoints == 0) { couple._1.setImage(new Image("view/tombstone2.png")) })
      opponentCharacterImages.foreach(couple =>
        if (couple._2.status.healthPoints == 0) { couple._1.setImage(new Image("view/tombstone1.png")) })
    }

    /** Adds the player's active character's moves to the listView
      *
      * @param character the character who's turn is
      */
    def setupCharacterMoves(character: Character): Unit = {
      moveList = FXCollections.observableArrayList()
      moveList.add(PhysicalAttackRepresentation)
      character.specialMoves.foreach(move =>
        moveList.add(move._1 + MovesSeparator + "MP: " + move._2.manaCost + " Max targets: " + move._2.maxTargets))
      moveListView.setItems(moveList)
    }

    def setTargets(imagePressed: ImageView, character: Character): Unit = {
      if (targetImages.exists(image => image.getId equals imagePressed.getId)) {
        targetImages -= imagePressed
        println(targetImages)
        targets -= character
        setCharacterUnselected(imagePressed)
      } else {
        targetImages += imagePressed
        println(targetImages)
        targets += character
        setCharacterSelected(imagePressed)
      }
    }

    /** Activates/Deactivates the act button */
    def actButtonActivation(): Unit = {
      if (canAct) {
        actButton.setDisable(false)
      } else {
        actButton.setDisable(true)
      }
    }

    /** Checks if the act button should be active:
      *
      * The act button is active if the player has targeted at least one character, and the
      * number of targets does not exceed the maximum number specified by the selected move.
      * Furthermore the active character needs to have enough mana points to use the said move.
      *
      * @return if the act button should be active or not
      */
    def canAct: Boolean = {
      if (moveListView.getSelectionModel.getSelectedItems.isEmpty || targetImages.isEmpty) {
        false
      } else {
        val moveName = moveListView.getSelectionModel.getSelectedItem.split(MovesSeparator).head
        var moveMaxTargets = 1
        moveName match {
          case PhysicalAttackRepresentation => targetImages.size <= moveMaxTargets
          case _ =>
            val selectedMove = activeCharacter.specialMoves(moveName)
            moveMaxTargets = selectedMove.maxTargets
            targetImages.size <= moveMaxTargets && Move.canMakeMove(activeCharacter, selectedMove)
        }
      }
    }

    /** Applies the selection effect to the targeted character's image
      *
      * @param charImage the character's image
      */
    def setCharacterSelected(charImage: ImageView): Unit = {
      charImage.setEffect(TargetedEffect)
    }

    /** Removes the selection effect to the deselected character's image
      *
      * @param charImage the character's image
      */
    def setCharacterUnselected(charImage: ImageView): Unit = {
      if (playerCharacterImages.contains(charImage) && playerCharacterImages(charImage) == activeCharacter) {
        charImage.setEffect(ActivePlayerEffect)
      } else {
        charImage.setEffect(null)
      }
    }
  }

  /** Handles the press of the act button
    *
    */
  @FXML def handleActButtonPress(): Unit = {
    Round.actCalculation(moveListView.getSelectionModel.getSelectedItem.split(MovesSeparator).head, targets.toList)
    activeLabel.setTextFill(Color.BLACK)
    targets = ListBuffer()
    targetImages.foreach(target => setCharacterUnselected(target))
    targetImages = ListBuffer()

    //Move.makeMove(activeCharacter.specialMoves(moveListView.getSelectionModel.getSelectedItem),activeCharacter,)
    //Compute the move and then send the new status message
  }

  @FXML def movesManualPressed(event: ActionEvent) {
    ApplicationView.createMovesManualView()
  }

  /** Checks if the act button is to be activated after the selection of a different move
    *
    * @param mouseEvent
    */
  @FXML def handleMoveSelection(mouseEvent: MouseEvent) {
    actButtonActivation()
  }

  /** Adds/Removes the pressed character to/from the target's list, and checks if the act button is to be activated
    *
    * @param mouseEvent
    */
  @FXML def handleCharacterToTargetPressed(mouseEvent: MouseEvent) {
    if (activeCharacter.owner.get == Battle.playerId) {
      val characterPressed: ImageView = mouseEvent.getSource.asInstanceOf[ImageView]
      if (characterPressed.getId.contains("player")) {
        setTargets(characterPressed, playerCharacterImages(characterPressed))
      } else {
        setTargets(characterPressed, opponentCharacterImages(characterPressed))
      }
      actButtonActivation()
    }
  }
}
