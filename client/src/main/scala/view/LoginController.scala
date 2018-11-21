package view

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label
import play.api.libs.json._
import ApplicationView.viewSelector._
import com.spingo.op_rabbit.{Message, PlayJsonSupport}
import com.spingo.op_rabbit.properties.ReplyTo

class LoginController extends Initializable with ViewController {
  implicit val dataFormat = Json.format[DataLoginRequestMessage]
  @FXML var counterLabel: Label = _

  @FXML def handleLoginAsGuest(event: ActionEvent) {
    println("pressed login as a guest")
    counterLabel.setText((Integer.valueOf(counterLabel.getText) + 1).toString)

    import main.LoginActor
    import PlayJsonSupport._
    LoginActor.rabbitControl ! Message(
      view.DataLoginRequestMessage(),
      LoginActor.publisher,
      Seq(ReplyTo(LoginActor.loginResponseQueue.queueName))
    )

    ApplicationView changeView TEAM
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
}

object LoginController {
  def apply(): LoginController = new LoginController
}
