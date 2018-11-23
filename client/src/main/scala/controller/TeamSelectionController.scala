package controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view.ApplicationView
import ApplicationView.viewSelector._

class TeamSelectionController extends Initializable with ViewController {

  @FXML def handleLogout(event: ActionEvent) {
    println("Logout pressed")

    ApplicationView changeView LOGIN
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object TeamSelectionController {
  def apply(): TeamSelectionController = new TeamSelectionController
}
