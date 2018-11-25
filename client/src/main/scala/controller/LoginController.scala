package controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view._
import ApplicationView.viewSelector._
import messaging.LoginManager

object LoginController extends Initializable with ViewController {
  val controller: ViewController = this

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("Login as a guest pressed")

    ApplicationView changeView WAITING

    LoginManager.loginRequest()
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}
