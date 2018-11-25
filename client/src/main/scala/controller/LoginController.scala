package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import play.api.libs.json._
import com.spingo.op_rabbit.{Message, PlayJsonSupport}
import com.spingo.op_rabbit.properties.ReplyTo
import messages._
import view._
import ApplicationView.viewSelector._
import messaging.LoginManager

class LoginController extends Initializable with ViewController {
  implicit val dataFormat =
    Json.format[LoginGuestRequest]
  var loginManager: LoginManager = new LoginManager

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("Login as a guest pressed")

    import PlayJsonSupport._
    loginManager.rabbitControl ! Message(
      messages.LoginGuestRequest(None),
      loginManager.publisher,
      Seq(ReplyTo(loginManager.loginGuestResponseQueue.queueName))
    )

    ApplicationView changeView WAITING
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object LoginController {

  def apply(): LoginController = new LoginController
}
