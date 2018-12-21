package controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import view._
import ViewConfiguration.ViewSelector._
import messaging.LoginManager

/** Controller of the login view.
  *
  * @author Daniele Schiavi
  */
object LoginController extends Initializable with ViewController {

  val controller: ViewController = this

  /** Handler of the "Login as a guest" button action. */
  @FXML def handleLoginAsGuest(event: ActionEvent) {
    ApplicationView changeView WAITING_TO_LOGIN
    LoginManager.loginAsGuestRequest()
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}
