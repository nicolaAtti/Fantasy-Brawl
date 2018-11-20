package view

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label
import play.api.libs.json._

class LoginController extends Initializable with ViewController {
  implicit val dataFormat = Json.format[Data]
  @FXML var counterLabel: Label = _

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("pressed login as a guest")
    counterLabel.setText((Integer.valueOf(counterLabel.getText) + 1).toString)

    ApplicationView changeView ApplicationView.viewSelector.TEAM
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object LoginController {
  def apply(): LoginController = new LoginController
}
