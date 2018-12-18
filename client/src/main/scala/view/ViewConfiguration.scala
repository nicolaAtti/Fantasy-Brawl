package view

import javafx.scene.control.Alert

/** Contains the configuration used for the views.
  *
  * @author Daniele Schiavi
  */
object ViewConfiguration {
  val WindowHeight: Double = 540
  val WindowWidth: Double = 960

  val DialogErrorType: Alert.AlertType = Alert.AlertType.ERROR
  val DialogErrorTitle: String = "Error"

  val LoginTitle: String = "Fantasy Brawl - Login"
  val LoginForm: String = "LoginForm.fxml"
  val TeamSelectionTitle: String = "Fantasy Brawl - Team Selection"
  val TeamSelectionForm: String = "TeamSelectionForm.fxml"
  val WaitingToLoginTitle: String = "Fantasy Brawl - Login"
  val WaitingToLoginForm: String = "WaitingToLoginForm.fxml"
  val WaitingOpponentTitle: String = "Fantasy Brawl - In matchmaking queue"
  val WaitingOpponentForm: String = "WaitingOpponentForm.fxml"
  val BattleTitle: String = "Fantasy Brawl - Battle"
  val BattleForm: String = "BattleForm.fxml"
  val MovesManualTitle: String = "Fantasy Brawl - Team Selection"
  val MovesManualForm: String = "MovesManualView.fxml"

  val MovesManualViewBackgroundImage: String = "/view/background4.jpg"

  /** Defines the acceptable views. */
  object ViewSelector extends Enumeration {
    val LOGIN, TEAM, WAITING_TO_LOGIN, WAITING_OPPONENT, BATTLE = Value
  }
  type ViewSelector = ViewSelector.Value
}
