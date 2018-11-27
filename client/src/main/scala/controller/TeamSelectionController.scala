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

object TeamSelectionController extends Initializable with ViewController {
  val controller: ViewController = this
  var username: String = "guest"

  @FXML var idLabel: Label = _
  @FXML var characterDescription: TextArea = _
  @FXML var selectedCharacter00: ImageView = _
  @FXML var chosenCharacter0: StackPane = _
  @FXML var grid: GridPane = _

  var selectedCharacter: ImageView = new ImageView()
  var chosenCharacter: StackPane = new StackPane()

  @FXML def handleLogout(event: ActionEvent) {
    println("Logout pressed")

    ApplicationView changeView LOGIN
  }

  @FXML def handleCharacterToChoosePressed(mouseEvent: MouseEvent) {
    val characterPressed: ImageView = mouseEvent.getSource.asInstanceOf[ImageView]
    println(characterPressed.getId + " pressed")
    var pane: StackPane = selectedCharacter.getParent.asInstanceOf[StackPane]
    pane.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    selectedCharacter = characterPressed
    pane = selectedCharacter.getParent.asInstanceOf[StackPane]
    pane.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
    characterDescription.setText(characterPressed.getId)

  }

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
    grid.getChildren.forEach(pane => {
      pane
        .asInstanceOf[StackPane]
        .setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)))
    })
    selectedCharacter = selectedCharacter00
    chosenCharacter = chosenCharacter0
    val pane: StackPane = selectedCharacter.getParent.asInstanceOf[StackPane]
    pane.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
    chosenCharacter.setBackground(
      new Background(new BackgroundFill(Paint.valueOf("BLUE"), CornerRadii.EMPTY, Insets.EMPTY)))
  }
}
