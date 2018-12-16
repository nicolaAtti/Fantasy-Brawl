package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label}
import javafx.scene.input.MouseEvent

object MoveManualController extends Initializable with ViewController {

  @FXML var meleeMoves: Label = _
  @FXML var rangedMoves: Label = _
  @FXML var spellMoves: Label = _

  @FXML var meleeDescriptions: Label = _
  @FXML var rangedDescriptions: Label = _
  @FXML var spellDescriptions: Label = _

  @FXML var meleeButton: Button = _
  @FXML var rangedButton: Button = _
  @FXML var spellButton: Button = _

  /** Return the LoginController. */
  val controller: ViewController = this

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    rangedMoves.setVisible(false)
    spellMoves.setVisible(false)
    rangedDescriptions.setVisible(false)
    spellDescriptions.setVisible(false)
    meleeButton.setDisable(true)
  }

  @FXML def meleeButtonPressed(mouseEvent: MouseEvent): Unit = {
    rangedMoves.setVisible(false)
    rangedDescriptions.setVisible(false)
    spellMoves.setVisible(false)
    spellDescriptions.setVisible(false)
    rangedButton.setDisable(false)
    spellButton.setDisable(false)

    meleeMoves.setVisible(true)
    meleeDescriptions.setVisible(true)
    meleeButton.setDisable(true)

  }

  @FXML def rangedButtonPressed(mouseEvent: MouseEvent): Unit = {
    meleeMoves.setVisible(false)
    meleeDescriptions.setVisible(false)
    spellMoves.setVisible(false)
    spellDescriptions.setVisible(false)
    meleeButton.setDisable(false)
    spellButton.setDisable(false)

    rangedMoves.setVisible(true)
    rangedDescriptions.setVisible(true)
    rangedButton.setDisable(true)
  }

  @FXML def spellButtonPressed(mouseEvent: MouseEvent): Unit = {
    meleeMoves.setVisible(false)
    meleeDescriptions.setVisible(false)
    rangedMoves.setVisible(false)
    rangedDescriptions.setVisible(false)
    meleeButton.setDisable(false)
    rangedButton.setDisable(false)

    spellMoves.setVisible(true)
    spellDescriptions.setVisible(true)
    spellButton.setDisable(true)
  }

}
