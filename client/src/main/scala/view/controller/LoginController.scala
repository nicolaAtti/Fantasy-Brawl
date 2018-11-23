package view.controller

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import play.api.libs.json._
import com.spingo.op_rabbit.{Message, PlayJsonSupport}
import com.spingo.op_rabbit.properties.ReplyTo
import view.DataLoginRequestMessage

class LoginController extends Initializable with ViewController {
  implicit val dataFormat = Json.format[DataLoginRequestMessage]

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("Pressed login as a guest")

    import main.LoginManager
    import PlayJsonSupport._
    LoginManager.rabbitControl ! Message(
      DataLoginRequestMessage(0),
      LoginManager.publisher,
      Seq(ReplyTo(LoginManager.loginResponseQueue.queueName))
    )
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object LoginController {
  def apply(): LoginController = new LoginController
}
