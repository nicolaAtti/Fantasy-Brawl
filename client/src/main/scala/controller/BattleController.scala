package controller

import java.net.URL
import java.util.ResourceBundle

import game.{Battle, Round}
import javafx.animation.{KeyFrame, Timeline}
import javafx.collections.{FXCollections, ObservableList}
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.effect.InnerShadow
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import model.Character

import scala.collection.mutable.ListBuffer

object BattleController extends Initializable with ViewController {

  val controller: ViewController = this

  final val SelectedEffect: InnerShadow = new InnerShadow(14.5, Color.BLUE)

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

  var myImages: List[ImageView] = List()
  var opponentImages: List[ImageView] = List()

  var targets: ListBuffer[ImageView] = ListBuffer()
  var moveList: ObservableList[String] = FXCollections.observableArrayList()
  var activeCharacter: Character = _

  var timeSeconds: Int = 60
  val timeline: Timeline = new Timeline()

  /**
    * Initializes the elements composing the Battle GUI
    *
    * @param location
    * @param resources
    */
  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    SelectedEffect.setHeight(30)
    SelectedEffect.setWidth(30)
    myImages = List(playerChar1Image, playerChar2Image, playerChar3Image, playerChar4Image)
    opponentImages = List(opponentChar1Image, opponentChar2Image, opponentChar3Image, opponentChar4Image)
    setBattlefield()

    timeline.setCycleCount(60)
    timeline.getKeyFrames.add(new KeyFrame(Duration.seconds(1), (_: ActionEvent) => {
      timeSeconds = timeSeconds - 1
      timerCounter.setText(timeSeconds.toString)
      if (timeSeconds <= 0) {
        timeline.stop()
        Round.endTurn()
      }
    }))
  }

  def resetTimer(): Unit ={
    timeSeconds = 60
    timeline.play()
  }

  /**
    * Setups the GUI for both teams
    *
    **/
  def setBattlefield(): Unit = {
    setupTeam(Battle.playerTeam, "Player")
    setupTeam(Battle.opponentTeam, "Opponent")
  }

  /**
    * Sets the player's team images and status description labels
    *
    * @param team  the team to setup
    * @param player the player owning the team
    */
  def setupTeam(team: Map[String, Character], player: String): Unit = player match {
    case "Player" =>
      //setupCharacterMoves(myTeamMembers("Jacob"))
      prepareImages(player)
      setupLabels(player)
    case "Opponent" =>
      prepareImages(player)
      setupLabels(player)
  }

  /**
    * Assigns to each character it's battle image, orientation depends on the player
    *
    * @param player the player which structure is iterated
    */
  private def prepareImages(player: String): Unit = player match {
    case "Player" =>
      (Battle.playerTeam.keys zip myImages).foreach(
        member =>
          member._2
            .setImage(new Image("view/" + member._1 + "1-" + "clean.png")))
    case "Opponent" =>
      (Battle.opponentTeam.keys zip opponentImages).foreach(
        member =>
          member._2
            .setImage(new Image("view/" + member._1 + "2-" + "clean.png")))
  }

  /**
    * Writes all the team's labels to display names, health and mana values and possible alterations for each character
    *
    * @param player the player which structure is iterated
    */
  def setupLabels(player: String): Unit = player match {
    case "Player" =>
      (Battle.playerTeam.keys zip playerCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1))
      (Battle.playerTeam.values zip playerHps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints))
      (Battle.playerTeam.values zip playerMps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints))
      (Battle.playerTeam.values zip playerAlterations.getChildren.toArray) foreach (couple =>
        couple._2
          .asInstanceOf[Label]
          .setText(couple._1.status.alterations.keySet.map(alt => alt.acronym).foldRight("")(_ + "/" + _).dropRight(1)))

    case "Opponent" =>
      (Battle.opponentTeam.keys zip opponentCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1))
      (Battle.opponentTeam.values zip opponentHps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints))
      (Battle.opponentTeam.values zip opponentMps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints))
      (Battle.opponentTeam.values zip opponentAlterations.getChildren.toArray) foreach (couple =>
        couple._2
          .asInstanceOf[Label]
          .setText(couple._1.status.alterations.keySet.map(alt => alt.acronym).foldRight("")(_ + "/" + _).dropRight(1)))
  }

  //TODO
  //Link with turn management
  def setActiveCharacter(character: Character): Unit = {
    activeCharacter = character
    resetTimer()
  }

  /**
    * Adds the active character's moves to the listView
    *
    * @param character the character who's turn is
    */
  def setupCharacterMoves(character: Character): Unit = {
    //Get the character's moves (if it's mine) that can act in the current turn
    moveList.add("Physical Attack")
    character.specialMoves.keySet.foreach(moveName => moveList.add(moveName))
    moveListView.setItems(moveList)
  }

  /**
    * Handles the press of the act button
    *
    */
  @FXML def handleActButtonPress(): Unit = ???

  /**
    * Checks if the act button is to be activated after the selection of a different move
    *
    * @param mouseEvent
    */
  @FXML def handleMoveSelection(mouseEvent: MouseEvent) {
    actButtonActivation()
  }

  /**
    * Adds/Removes the pressed character to/from the target's list, and checks if the act button is to be activated
    *
    * @param mouseEvent
    */
  @FXML def handleCharacterToTargetPressed(mouseEvent: MouseEvent) {
    var characterPressed: ImageView = mouseEvent.getSource.asInstanceOf[ImageView]
    if (targets.contains(characterPressed)) {
      targets -= characterPressed
      setCharacterUnselected(characterPressed)
    } else {
      targets += characterPressed
      setCharacterSelected(characterPressed)
    }
    actButtonActivation()
  }

  //TODO
  //Link with turn manager
  def addAlteration(): Unit = ???

  /**
    * Activates/Deactivates the act button
    */
  private def actButtonActivation(): Unit = {
    if (!cannotAct) {
      actButton.setDisable(false)
    } else {
      actButton.setDisable(true)
    }
  }

  /**
    * Checks if the act button should be active
    * @return if the act button should be active or not
    */
  private def cannotAct: Boolean = {
    if (!moveListView.getSelectionModel.getSelectedItems.isEmpty && targets.nonEmpty) {
      val moveName = moveListView.getSelectionModel.getSelectedItem
      var moveMaxTargets = 1
      moveName match {
        case "Physical Attack" => targets.size > moveMaxTargets
        case _ =>
          moveMaxTargets = activeCharacter.specialMoves(moveName).maxTargets
          targets.size > moveMaxTargets
      }
    } else {
      true
    }
  }

  /**
    * Applies the selection effect to the targeted character's image
    * @param charImage the character's image
    */
  private def setCharacterSelected(charImage: ImageView): Unit = {
    charImage.setEffect(SelectedEffect)
  }

  /**
    * Removes the selection effect to the deselected character's image
    * @param charImage the character's image
    */
  private def setCharacterUnselected(charImage: ImageView): Unit = {
    charImage.setEffect(null)
  }

}
