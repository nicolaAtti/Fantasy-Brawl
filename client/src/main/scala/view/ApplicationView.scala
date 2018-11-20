package view

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

object ApplicationView {
  private var stage: Stage = new Stage()
  object viewSelector extends Enumeration {
    val LOGIN, TEAM = Value
  }
  type ViewSelector = viewSelector.Value

  // factory
  import viewSelector._
  def changeView(view: ViewSelector): Unit = view match {
    case LOGIN =>
      setupScene(title = ViewTitle.login,
                 form = ViewForm.login,
                 controller = LoginController())
    case TEAM =>
      setupScene(title = ViewTitle.teamSelection,
                 form = ViewForm.teamSelection,
                 controller = TeamSelectionController())
    case _ => hideView()
  }

  def setupStage(stage: Stage, minHeight: Double, minWidth: Double): Unit = {
    this.stage = stage
    stage setMinHeight minHeight
    stage setMinWidth minWidth
    stage setOnCloseRequest (_ => {
      Platform exit ()
      System exit 0
    })
  }

  def showView(): Unit = stage show ()

  def hideView(): Unit = stage hide ()

  private def setupScene(title: String,
                         form: String,
                         controller: ViewController) {
    val loader: FXMLLoader = new FXMLLoader(getClass getResource form)
    loader setController controller
    val scene: Scene = new Scene(loader.load())
    stage setTitle title
    stage setScene scene
  }
}
