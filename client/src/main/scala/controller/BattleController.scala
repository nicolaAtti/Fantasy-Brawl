package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.animation.{KeyFrame, Timeline}
import javafx.collections.{FXCollections, ObservableList}
import javafx.event.ActionEvent
import utilities.ScalaProlog._
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.effect.InnerShadow
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import model.{Alteration, Character, Move}

import scala.collection.mutable.ListBuffer

object BattleController extends Initializable with ViewController {

  val controller: ViewController = this

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

  private var playerTeam: Seq[String] = Seq()
  private var opponentTeam: Seq[String] = Seq()

  //Could go in battle
  var myTeamMoves: Map[String, Seq[Move]] = Map()
  var myTeamMembers: Map[String, Character] = Map()
  var opponentTeamMembers: Map[String, Character] = Map()
  var targets: ListBuffer[ImageView] = ListBuffer()
  var moveList: ObservableList[String] = FXCollections.observableArrayList()

  var activeCharacter: Character = _

  final val SelectedEffect: InnerShadow = new InnerShadow(14.5, Color.BLUE)

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    SelectedEffect.setHeight(30)
    SelectedEffect.setWidth(30)
    myImages = List(playerChar1Image, playerChar2Image, playerChar3Image, playerChar4Image)
    opponentImages = List(opponentChar1Image, opponentChar2Image, opponentChar3Image, opponentChar4Image)
    var timeSeconds: Int = 60
    val timeline: Timeline = new Timeline()
    timeline.setCycleCount(60)
    timeline.getKeyFrames.add(new KeyFrame(Duration.seconds(1), (_: ActionEvent) => {
      timeSeconds = timeSeconds - 1
      timerCounter.setText(timeSeconds.toString)
      if (timeSeconds <= 0) timeline.stop()
    }))
    timeline.play()
    setBattlefield()

    setActiveCharacter("Jacob")
  }

  /**
    * Setups the GUI depending on the characters chosen by each player (Should call submethods)
    *
    **/
  def setBattlefield(): Unit = {
    setupTeam(playerTeam, "Player")
    setupTeam(opponentTeam, "Opponent")
  }

  def setTeams(player: Seq[String], opponent: Seq[String]): Unit = {
    playerTeam = player
    opponentTeam = opponent
  }

  /**
    * Populates the player's team map
    *
    * @param team
    * @param player
    */
  def setupTeam(team: Seq[String], player: String): Unit = player match {
    case "Player" =>
      myTeamMembers = team.map(charName => (charName, createCharacter(charName))).toMap
      setupCharacterMoves(myTeamMembers("Jacob"))
      prepareImages(player)
      setupLabels(player)
    case "Opponent" =>
      opponentTeamMembers = team.map(charName => (charName, createCharacter(charName))).toMap
      prepareImages(player)
      setupLabels(player)
  }

  //Should go in model
  def createCharacter(charName: String): Character = {
    getCharacter(charName)
  }

  def prepareImages(player: String): Unit = player match {
    case "Player" =>
      (myTeamMembers.keys zip myImages).foreach(
        member =>
          member._2
            .setImage(new Image("view/" + member._1 + "1-" + "clean.png")))
    case "Opponent" =>
      (opponentTeamMembers.keys zip opponentImages).foreach(
        member =>
          member._2
            .setImage(new Image("view/" + member._1 + "2-" + "clean.png")))
  }

  def setupLabels(player: String): Unit = player match {
    case "Player" =>
      (myTeamMembers.keys zip playerCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1))
      (myTeamMembers.values zip playerHps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints))
      (myTeamMembers.values zip playerMps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints))
      (myTeamMembers.values zip playerAlterations.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(getAfflictionAcronyms(couple._1.status.alterations.keySet)))

    case "Opponent" =>
      (opponentTeamMembers.keys zip opponentCharNames.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1))
      (opponentTeamMembers.values zip opponentHps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints))
      (opponentTeamMembers.values zip opponentMps.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints))
      (opponentTeamMembers.values zip opponentAlterations.getChildren.toArray) foreach (couple =>
        couple._2.asInstanceOf[Label].setText(getAfflictionAcronyms(couple._1.status.alterations.keySet)))
  }

  private def getAfflictionAcronyms(alterations: Set[Alteration]): String = {
    val labelText: String = "/"
    if (alterations.nonEmpty) {
      alterations.foreach(alt =>
        alt.getClass.getCanonicalName match {
          case "Stunned"      => labelText.concat("Stn/")
          case "Frozen"       => labelText.concat("Frz/")
          case "Blinded"      => labelText.concat("Bln/")
          case "Asleep"       => labelText.concat("Slp/")
          case "Regeneration" => labelText.concat("Reg/")
          case "Berserk"      => labelText.concat("Brk/")
          case "Silenced"     => labelText.concat("Sil/")
          case "Poisoned"     => labelText.concat("Psn/")
      })
    }
    labelText
  }

  //TODO
  //Link with turn management
  def setActiveCharacter(characterName: String): Unit = {
    activeCharacter = myTeamMembers("Jacob")
  }

  /**
    * Adds the character's moves to the map
    *
    * @param characterName
    */
  def setupCharacterMoves(character: Character): Unit = {
    //Get the character's moves (if it's mine) that can act in the current turn
    moveList.add("Physical Attack")
    character.specialMoves.keySet.foreach(moveName => moveList.add(moveName))
    moveListView.setItems(moveList)
  }

  @FXML def handleActButtonPress(): Unit = ???

  @FXML def handleMoveSelection(mouseEvent: MouseEvent) {
    actButtonActivation()
  }

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

  private def actButtonActivation(): Unit = {
    if (!cannotAct) {
      actButton.setDisable(false)
    } else {
      actButton.setDisable(true)
    }
  }

  private def cannotAct: Boolean = {
    if (!moveListView.getSelectionModel.getSelectedItems.isEmpty) {
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

  private def setCharacterSelected(charImage: ImageView): Unit = {
    charImage.setEffect(SelectedEffect)
  }

  private def setCharacterUnselected(charImage: ImageView): Unit = {
    charImage.setEffect(null)
  }

}
