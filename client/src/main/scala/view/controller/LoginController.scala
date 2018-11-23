package view.controller

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import play.api.libs.json._
import com.spingo.op_rabbit.{Message, PlayJsonSupport}
import com.spingo.op_rabbit.properties.ReplyTo
import messages._

class LoginController extends Initializable with ViewController {
  implicit val dataFormat = Json.format[LoginGuestRequest]

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("Pressed login as a guest")

    import main.LoginManager
    import PlayJsonSupport._
    LoginManager.rabbitControl ! Message(
      messages.LoginGuestRequest(None),
      LoginManager.publisher,
      Seq(ReplyTo(LoginManager.loginGuestResponseQueue.queueName))
    )
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object LoginController {
  def apply(): LoginController = new LoginController
}
