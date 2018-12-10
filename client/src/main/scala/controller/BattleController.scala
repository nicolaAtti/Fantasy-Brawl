package controller

import java.awt.{Button, Label}
import java.io.File
import java.net.URL
import java.util.ResourceBundle

import utilities.ScalaProlog._
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.ListView
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.VBox
import model.{Character, Move}

object BattleController extends Initializable with ViewController {

  val controller: ViewController = this

  @FXML var playerCharNames: VBox = _
  @FXML var playerHps: VBox = _
  @FXML var playerMps: VBox = _
  @FXML var playerAfflictions: VBox = _

  @FXML var opponentCharNames: VBox = _
  @FXML var opponentHps: VBox = _
  @FXML var opponentMps: VBox = _
  @FXML var opponentAfflictions: VBox = _

  @FXML var playerChar1Image: ImageView = _
  @FXML var playerChar2Image: ImageView = _
  @FXML var playerChar3Image: ImageView = _
  @FXML var playerChar4Image: ImageView = _

  @FXML var opponentChar1Image: ImageView = _
  @FXML var opponentChar2Image: ImageView = _
  @FXML var opponentChar3Image: ImageView = _
  @FXML var opponentChar4Image: ImageView = _

  var myImages: List[ImageView] = List()
  var opponentImages: List[ImageView] = List()

  private var playerTeam: Seq[String] = Seq()
  private var opponentTeam: Seq[String] = Seq()

  var myTeamMoves: Map[String, Seq[Move]] = Map()
  var myTeamMembers: Map[String, Character] = Map()
  var opponentTeamMembers: Map[String, Character] = Map()

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    println(playerCharNames)
    println(playerCharNames.getChildren)
    println(playerCharNames.getChildren.toArray)
    myImages = List(playerChar1Image, playerChar2Image, playerChar3Image, playerChar4Image)
    opponentImages = List(opponentChar1Image, opponentChar2Image, opponentChar3Image, opponentChar4Image)
    setBattlefield()
  }

  /**
    * Setups the GUI depending on the characters chosen by each player (Should call submethods)
    *
    **/
  def setBattlefield(): Unit = {
    setupTeam(playerTeam, "Player")
    setupTeam(opponentTeam, "Opponent")
  }

  def setTeams(player: Seq[String],opponent: Seq[String]): Unit ={
    playerTeam = player
    opponentTeam = opponent
  }


  /**
    * Populates the player's team map
    *
    * @param team
    * @param player
    */
  def setupTeam(team: Seq[String], player: String): Unit = player match {
    case "Player" =>
      myTeamMembers = team.map(charName => (charName, createCharacter(charName))).toMap
      prepareImages(player)
      //setupLabels(player)
    case "Opponent" =>
      opponentTeamMembers = team.map(charName => (charName, createCharacter(charName))).toMap
      prepareImages(player)
      //setupLabels(player)
  }

  def createCharacter(charName: String): Character = {
    val charInfo = getCharacter(charName)
    Character(
      extractString(charInfo, "Class"),
      charName,
      charInfo
    )
  }

  def prepareImages(player: String): Unit = player match {
    case "Player" =>
      (myTeamMembers.keys zip myImages).foreach(member => member._2.setImage(new Image("view/"+member._1 + "1-" + "clean.png")))/*(new File(member._1 + "1-" + "clean.png")*/
    case "Opponent" =>
      (opponentTeamMembers.keys zip opponentImages).foreach(member =>
        member._2.setImage(new Image("view/"+member._1 + "2-" + "clean.png")))/*(new File(member._1 + "1-" + "clean.png")*/
  }

  def setupLabels(player: String): Unit = player match {
    case "Player" =>
      println(playerCharNames)
      println(playerCharNames.getChildren)
      println(playerCharNames.getChildren.toArray)
      (myTeamMembers.keys zip playerCharNames.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1)
        })
      (myTeamMembers.values zip playerHps.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints)
        })
      (myTeamMembers.values zip playerMps.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints)
        })
    case "Opponent" =>
      opponentCharNames.getChildren.toArray.foreach(println(_))
      opponentTeamMembers.foreach(println(_))
      (opponentTeamMembers.keys zip opponentCharNames.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1)
        })
      (opponentTeamMembers.values zip opponentHps.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1.status.healthPoints + "/" + couple._1.status.maxHealthPoints)
        })
      (opponentTeamMembers.values zip opponentMps.getChildren.toArray) foreach (couple =>
        couple._2 match {
          case label: Label => label.setText(couple._1.status.manaPoints + "/" + couple._1.status.maxManaPoints)
        })
  }

  /**
    * Adds the character's moves to the map
    *
    * @param characterName
    */
  def setupCharacterMoves(characterName: String): Unit = ???

  def handleActButtonPress(): Unit = ???

  def handleCharacterSelection(): Unit = ???

}
