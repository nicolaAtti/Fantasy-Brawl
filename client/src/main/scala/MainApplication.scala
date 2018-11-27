import javafx.application.Application
import javafx.stage.Stage
import view.ApplicationView.viewSelector._

class MainApplication extends Application {

  /**
    * Create and show the login view.
    */
  override def start(primaryStage: Stage): Unit = {
    import view._
    ApplicationView setupStage (primaryStage, 540, 960)
    ApplicationView changeView LOGIN
    ApplicationView showView ()
  }
}

/**
  * Starting point of the client application.
  *
  * @author Daniele Schiavi
  */
object MainApplication {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[MainApplication], args: _*)
  }
}
