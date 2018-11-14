package main.scala.model

//import main.resources.Utility
import model.{Statistics, StatModifiers}

import scala.math._

//Aspetti condivisi : le 5 statistiche (che vanno nel costruttore) i metodi per accedervici ,
//Aspetti differenti: i modificatori con cui vengono aleterati i valori delle statistiche secondarie

/**
  * Default implementation of a game Character
  *
  * @param charName   the name of the Character
  * @param stats      the statistics of the Character
  * @author Nicola Atti
  */
trait Character{

  var charName: String
  var stats: Statistics
  var statMods: StatModifiers


  def getPhysDamage: Int = floor(stats.getStrength * statMods.getStrMod)toInt
  def getPysCritDamage: Int = floor((stats.getStrength * statMods.getStrMod)/5 +150)toInt
  def getSpeed: Int = floor((stats.getAgility * statMods.getAgiMod)/10)toInt
  def getCritChance: Int = floor((stats.getAgility * statMods.getAgiMod)/2)toInt
  def getMagDefence: Int = floor(stats.getSpirit * statMods.getSpiMod)toInt
  def getMaxMP: Int = floor((stats.getSpirit * statMods.getSpiMod)*5)toInt
  def getMagDamage: Int = floor(stats.getIntelligence * statMods.getIntMod)toInt
  def getMagicCritDamage: Int = floor((stats.getIntelligence * statMods.getIntMod)/10 +150)toInt
  def getMaxHP: Int = floor((stats.getResistance * statMods.getHpMod)*10)toInt
  def getPhysDefence: Int = floor(stats.getResistance * statMods.getResMod)toInt

  //Passagli la funzione della mossa, sia essa speciale, attacco base o passare il turno   STRATEGY
  def doMove = ???

  /*def changeCurrHPMP(stat: String,function: (Int,Int)=>Int , value: Int): Unit = stat match {
    case "HP" => if (function(currHP, value) > maxHP) {
      currHP = maxHP
    } else if (function(currHP, value) < 0) {
      currHP = 0
    } else {
      currHP = function(currHP, value)
    }
    case "MP" => if (function(currMP, value) > maxMP) {
      currMP = maxMP
    } else if (function(currMP, value) < 0) {
      currMP = 0
    } else {
      currMP = function(currMP, value)
    }
  }*/
}

private case class Warrior(override var charName: String, override var stats: Statistics, override var statMods: StatModifiers) extends Character {
}

private case class Thief(override var charName: String, override var stats: Statistics, override var statMods: StatModifiers) extends Character {
}
private case class Wizard(override var charName: String, override var stats: Statistics, override var statMods: StatModifiers) extends Character {
}
private case class Healer(override var charName: String,override var stats: Statistics, override var statMods: StatModifiers) extends Character {
}

//Companion object
object CharacterFactory{
        def apply(role: String,charName: String,stats: Statistics,mods: StatModifiers): Character = role match{
                case "Warrior" => new Warrior(charName,stats,mods)
                case "Thief" => new Thief(charName,stats,mods)
                case "Wizard" => new Wizard(charName,stats,mods)
                case "Healer" => new Healer(charName,stats,mods)
        }

}
