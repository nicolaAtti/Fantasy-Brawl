package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, ChoiceBox, Label}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

/** The controller for the move manual GUI
  *
  * @author Nicola Atti
  */
object MoveManualController extends Initializable with ViewController {
  @FXML var characterChoiceBox: ChoiceBox[String] = _
  @FXML var labelPane: Pane = _
  @FXML var choiceButton: Button = _

  val characterList: ObservableList[String] = FXCollections.observableArrayList("Jacob",
                                                                                "Annabelle",
                                                                                "Albert",
                                                                                "Lidya",
                                                                                "Noah",
                                                                                "Cassandra",
                                                                                "Linn",
                                                                                "Aster",
                                                                                "Norman",
                                                                                "Fernando",
                                                                                "Rikh",
                                                                                "Nora")
  var characterLabelMap: Map[AnyRef, AnyRef] = Map()
  var actualSelected: String = _

  /** Return the LoginController. */
  val controller: ViewController = this

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    characterChoiceBox.setItems(characterList)
    characterChoiceBox.getSelectionModel.selectFirst()
    actualSelected = characterChoiceBox.getSelectionModel.getSelectedItem
    characterLabelMap = (characterList.toArray zip labelPane.getChildren.toArray).toMap
  }

  @FXML def handleChoiceBoxSelection(mouseEvent: MouseEvent): Unit = {
    val selected = characterChoiceBox.getSelectionModel.getSelectedItem
    characterLabelMap(actualSelected).asInstanceOf[Label].setVisible(false)
    characterLabelMap(selected).asInstanceOf[Label].setVisible(true)
    actualSelected = selected
  }
}
