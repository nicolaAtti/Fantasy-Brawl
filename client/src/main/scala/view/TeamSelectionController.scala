package view

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import play.api.libs.json._

class TeamSelectionController extends Initializable with ViewController {
  implicit val dataFormat = Json.format[Data]
  //@FXML var counterLabel: Label = _

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("pressed button")

    ApplicationView changeView ApplicationView.viewSelector.LOGIN
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object TeamSelectionController {
  def apply(): TeamSelectionController = new TeamSelectionController
}
