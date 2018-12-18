import javafx.application.Application
import javafx.stage.Stage
import messaging.{LoginManager, MatchmakingManager, RoundManager}
import view.ViewConfiguration.ViewSelector._

class MainApplication extends Application {

  /** Create and show the login view. */
  override def start(primaryStage: Stage): Unit = {
    import view._
    ApplicationView setupStage (primaryStage, ViewConfiguration.WindowHeight, ViewConfiguration.WindowWidth)
    ApplicationView changeView LOGIN
    ApplicationView showView ()
  }
}

/** Starting point of the client application.
  *
  * @author Daniele Schiavi
  */
object MainApplication {

  def main(args: Array[String]): Unit = {
    LoginManager.start()
    MatchmakingManager.start()
    RoundManager.start()
    Application.launch(classOf[MainApplication], args: _*)
  }
}
