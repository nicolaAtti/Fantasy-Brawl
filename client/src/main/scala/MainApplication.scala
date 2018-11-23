import javafx.application.Application
import javafx.stage.Stage
import view.ApplicationView.viewSelector._

class MainApplication extends Application {
  override def start(primaryStage: Stage): Unit = {
    import view._
    ApplicationView setupStage (primaryStage, 648.0, 1152.0)
    ApplicationView changeView LOGIN
    ApplicationView showView ()
  }
}

object MainApplication {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[MainApplication], args: _*)
  }
}
