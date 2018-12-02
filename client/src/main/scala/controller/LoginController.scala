package controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view._
import ViewConfiguration.viewSelector._
import messaging.LoginManager

/**
  * Controller of the login view.
  *
  * @author Daniele Schiavi
  */
object LoginController extends Initializable with ViewController {

  /**
    * @return the controller
    */
  val controller: ViewController = this

  /**
    * Pressure handler of the "Login as a guest" button.
    */
  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("Login as a guest pressed")

    ApplicationView changeView WAITING_TO_LOGIN

    LoginManager.loginAsGuestRequest()
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}
