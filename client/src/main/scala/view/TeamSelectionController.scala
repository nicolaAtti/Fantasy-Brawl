package view

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import ApplicationView.viewSelector._

class TeamSelectionController extends Initializable with ViewController {

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("pressed button")

    ApplicationView changeView LOGIN
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object TeamSelectionController {
  def apply(): TeamSelectionController = new TeamSelectionController
}
