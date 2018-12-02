package view

/**
  * Contains the configuration used for the views.
  *
  * @author Daniele Schiavi
  */
object ViewConfiguration {
  val LoginTitle: String = "Fantasy Brawl - Login"
  val LoginForm: String = "LoginForm.fxml"
  val TeamSelectionTitle: String = "Fantasy Brawl - Team Selection"
  val TeamSelectionForm: String = "TeamSelectionForm.fxml"
  val WaitingToLoginTitle: String = "Fantasy Brawl - Login"
  val WaitingToLoginForm: String = "WaitingToLoginForm.fxml"
  val WaitingOpponentTitle: String = "Fantasy Brawl - In matchmaking queue"
  val WaitingOpponentForm: String = "WaitingOpponentForm.fxml"

  /**
    * Defines the acceptable views.
    */
  object viewSelector extends Enumeration {
    val LOGIN, TEAM, WAITING_TO_LOGIN, WAITING_OPPONENT = Value
  }
}
