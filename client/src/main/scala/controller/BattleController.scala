package controller

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.Initializable
import model.Move

object BattleController extends Initializable with ViewController{

  var myTeamMoves: Map[String,Seq[Move]] = Map()
  var myTeamMembers: Map[String,Character] = Map()
  var opponentTeamMembers: Map[String,Character] = Map()

  override def initialize(location: URL, resources: ResourceBundle): Unit = ???

  /**
    * Setups the GUI depending on the characters chosen by each player (Should call submethods)
    *
    * @param playerTeam
    * @param opponentTeam
    */
  def setBattlefield(playerTeam: Seq[String],opponentTeam: Seq[String]): Unit = ???

  /**
    * Populates the player's team map
    *
    * @param team
    */
  def setupMyTeam(team: Seq[String]): Unit = ???

  /**
    * Initialize the opponent's team map
    *
    * @param team
    */
  def setupOpponentTeam(team: Seq[String]): Unit = ???

  /**
    * Adds the character's moves to the map
    *
    * @param characterName
    */
  def setupCharacterMoves(characterName: String): Unit = ???

  def handleActButtonPress():Unit = ???

  def handleCharacterSelection():Unit = ???



}
