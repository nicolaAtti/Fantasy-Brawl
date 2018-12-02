package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view.ApplicationView
import view.ViewConfiguration.viewSelector._
import alice.tuprolog.SolveInfo
import javafx.geometry.Insets
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout._
import javafx.scene.paint.Paint
import messaging.MatchmakingManager

/**
  * Controller of the team selection view.
  *
  * @author Daniele Schiavi
  */
object TeamSelectionController extends Initializable with ViewController {
  val controller: ViewController = this
  var username: String = "guest"

  @FXML var idLabel: Label = _
  @FXML var characterDescription: TextArea = _
  @FXML var Jacob: StackPane = _
  @FXML var chosenCharacter0: StackPane = _
  @FXML var gridChosen: GridPane = _
  @FXML var gridToChoose: GridPane = _
  @FXML var joinCasualMatch: Button = _

  private var selectedCharacter: StackPane = new StackPane()
  private var chosenCharacter: StackPane = new StackPane()
  private var team: Map[String, StackPane] = Map()
  private val NotChosenSaturation: Double = 0
  private val NotChosenBrightness: Double = 0
  private val ChosenSaturation: Double = -1
  private val ChosenBrightness: Double = 0.7

  /**
    * Pressure handler of the "Logout" button.
    */
  @FXML def handleLogout(event: ActionEvent) {
    println("Logout pressed")

    ApplicationView changeView LOGIN
  }

  @FXML def handleJoinCasualQueue(event: ActionEvent) {
    println("Join Casual Queue pressed")

    ApplicationView changeView WAITING_OPPONENT

    var teamName: Map[String, String] = Map()
    team.foreach(character => {
      teamName += (character._1 -> character._2.getId)
    })
    MatchmakingManager.joinCasualQueueRequest(username, teamName)
  }

  /**
    * Pressure handler of the characters to choose from.
    */
  @FXML def handleCharacterToChoosePressed(mouseEvent: MouseEvent) {
    val characterPressed: StackPane = mouseEvent.getSource.asInstanceOf[StackPane]
    changeCharacterToChoose(selectedCharacter, characterPressed)
  }

  /**
    * Pressure handler of the chosen characters.
    */
  @FXML def handleCharacterChosenPressed(mouseEvent: MouseEvent) {
    val characterPressed: StackPane = mouseEvent.getSource.asInstanceOf[StackPane]
    changeCharacterChosen(chosenCharacter, characterPressed)
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    idLabel setText username
    deselectAllCharacters(gridChosen, gridToChoose)
    chosenCharacter = chosenCharacter0
    selectedCharacter = Jacob
    selectCharacter(chosenCharacter)
    changeCharacterToChoose(selectedCharacter, Jacob)
  }

  private def setDescription(characterName: String): Unit = {
    import utilities.ScalaProlog._
    val solveInfo: SolveInfo = getCharacter(characterName)
    var description: String = "Character name: " + characterName + "\n" +
      "Character class: " + extractString(solveInfo, "Class") + "\n\n" +
      "Strength: " + extractInt(solveInfo, "Strength") + "\n" +
      "Agility: " + extractInt(solveInfo, "Agility") + "\n" +
      "Spirit: " + extractInt(solveInfo, "Spirit") + "\n" +
      "Intelligence: " + extractInt(solveInfo, "Intelligence") + "\n" +
      "Resistance: " + extractInt(solveInfo, "Resistence") + "\n\n" +
      "MoveList: \n"
    extractList(solveInfo, "MoveList").foreach(move => {
      description += "    " + move +
        "     -> Type: " + extractString(getMove(move), "Type") +
        ", Base damage: " + extractInt(getMove(move), "BaseValue") +
        ", Mana cost: " + extractInt(getMove(move), "MPCost") +
        "\n"
    })
    characterDescription.setText(description)
  }

  private def deselectAllCharacters(gridPanes: GridPane*): Unit = {
    gridPanes.foreach(gridPane =>
      gridPane.getChildren.forEach(pane => {
        deselectCharacter(pane.asInstanceOf[StackPane])
      }))
  }

  private def changeCharacterToChoose(previous: StackPane, next: StackPane): Unit = {
    changeSelectedCharacter(previous, next)
    selectedCharacter = next
    setDescription(next.getId)
    if (!team.exists(_._2 equals next)) {
      chosenCharacter.getChildren
        .get(0)
        .asInstanceOf[ImageView]
        .setImage(selectedCharacter.getChildren.get(0).asInstanceOf[ImageView].getImage)
      team += (chosenCharacter.getId -> next)
      if (team.size == 4)
        joinCasualMatch.setDisable(false)
    }
  }

  private def changeCharacterChosen(previous: StackPane, next: StackPane): Unit = {
    changeSelectedCharacter(previous, next)
    chosenCharacter = next

    team.foreach(character => {
      val effect: ColorAdjust = new ColorAdjust()
      if (character._1 equals chosenCharacter.getId) {
        effect setSaturation NotChosenSaturation
        effect setBrightness NotChosenBrightness
        changeCharacterToChoose(selectedCharacter, character._2)
      } else {
        effect setSaturation ChosenSaturation
        effect setBrightness ChosenBrightness
      }
      character._2.getChildren.get(0).asInstanceOf[ImageView] setEffect effect
    })
  }

  private def changeSelectedCharacter(previous: StackPane, next: StackPane): Unit = {
    deselectCharacter(previous)
    selectCharacter(next)
  }

  private def selectCharacter(stackPane: StackPane): Unit = {
    stackPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
  }

  private def deselectCharacter(stackPane: StackPane): Unit = {
    stackPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
  }
}
