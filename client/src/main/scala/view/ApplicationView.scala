package view

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import controller._
import ViewConfiguration._
import messaging.MatchmakingManager

/** Manages the graphical interface.
  *
  * @author Daniele Schiavi
  */
object ApplicationView {
  private var stage: Stage = new Stage()

  private type ViewSelector = viewSelector.Value
  import viewSelector._

  /** Changes the current view to the required view.
    *
    * @param view the view to show
    */
  def changeView(view: ViewSelector): Unit = view match {
    case LOGIN            => setupScene(LoginTitle, LoginForm, Some(LoginController.controller))
    case TEAM             => setupScene(TeamSelectionTitle, TeamSelectionForm, Some(TeamSelectionController.controller))
    case WAITING_TO_LOGIN => setupScene(WaitingToLoginTitle, WaitingToLoginForm, None)
    case WAITING_OPPONENT => setupScene(WaitingOpponentTitle, WaitingOpponentForm, None)
    case _                => hideView()
  }

  /** Setup of the graphical window.
    *
    * @param stage the current window
    * @param height the height of the window
    * @param width the width of the window
    */
  def setupStage(stage: Stage, height: Double, width: Double): Unit = {
    this.stage = stage
    stage setHeight height
    stage setWidth width
    stage setResizable false
    stage setOnCloseRequest (_ => {
      if (stage.getTitle equals WaitingOpponentTitle) {
        MatchmakingManager.leaveCasualQueueRequest(TeamSelectionController.username)
      }
      Platform exit ()
      System exit 0
    })
  }

  /** Shows the current view. */
  def showView(): Unit =
    Platform runLater (() => {
      stage show ()
    })

  /** Hides the current view. */
  def hideView(): Unit =
    Platform runLater (() => {
      stage hide ()
    })

  private def setupScene(title: String, form: String, controller: Option[ViewController]) {
    val loader: FXMLLoader = new FXMLLoader(getClass getResource form)
    controller match {
      case Some(c) => loader setController c
      case _       => Unit
    }
    val scene: Scene = new Scene(loader.load())
    Platform runLater (() => {
      stage setTitle title
      stage setScene scene
    })
  }
}
