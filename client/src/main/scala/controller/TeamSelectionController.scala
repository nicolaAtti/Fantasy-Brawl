package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view.ApplicationView
import ApplicationView.viewSelector._
import javafx.geometry.Insets
import javafx.scene.control.{Label, TextArea}
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout._
import javafx.scene.paint.Paint

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
  @FXML var selectedCharacter00: StackPane = _
  @FXML var chosenCharacter0: StackPane = _
  @FXML var gridChosen: GridPane = _
  @FXML var gridToChoose: GridPane = _

  private var selectedCharacter: StackPane = new StackPane()
  private var chosenCharacter: StackPane = new StackPane()

  /**
    * Pressure handler of the "Logout" button.
    */
  @FXML def handleLogout(event: ActionEvent) {
    println("Logout pressed")

    ApplicationView changeView LOGIN
  }

  /**
    * Pressure handler of the characters to choose from.
    */
  @FXML def handleCharacterToChoosePressed(mouseEvent: MouseEvent) {
    val characterPressed: StackPane = mouseEvent.getSource.asInstanceOf[StackPane]
    println(characterPressed.getId + " pressed")
    selectedCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    selectedCharacter = characterPressed
    selectedCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
    chosenCharacter.getChildren
      .get(0)
      .asInstanceOf[ImageView]
      .setImage(selectedCharacter.getChildren.get(0).asInstanceOf[ImageView].getImage)
    characterDescription.setText(characterPressed.getId)
  }

  /**
    * Pressure handler of the chosen characters.
    */
  @FXML def handleCharacterChosenPressed(mouseEvent: MouseEvent) {
    val characterPressed: StackPane = mouseEvent.getSource.asInstanceOf[StackPane]
    println(characterPressed.getId + " pressed")
    chosenCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    chosenCharacter = characterPressed
    chosenCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
    characterDescription.setText(characterPressed.getId)
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    idLabel setText username
    gridChosen.getChildren.forEach(pane => {
      pane
        .asInstanceOf[StackPane]
        .setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    })
    gridToChoose.getChildren.forEach(pane => {
      pane
        .asInstanceOf[StackPane]
        .setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    })
    selectedCharacter = selectedCharacter00
    chosenCharacter = chosenCharacter0
    selectedCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
    chosenCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
  }
}
