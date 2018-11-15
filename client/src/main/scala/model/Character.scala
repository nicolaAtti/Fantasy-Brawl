package model

import main.resources.Utility

//Aspetti condivisi : le 5 statistiche (che vanno nel costruttore) i metodi per accedervici ,
//Aspetti differenti: i modificatori con cui vengono aleterati i valori delle statistiche secondarie

/**
  * Default implementation of a game Character
  *
  * @param charName   the name of the Character
  * @param stats      the statistics of the Character
  * @param statMods   the class statistic modifier
  * @author Nicola Atti
  */
trait Character{

  var charName: String
  var stats: Statistics
  var statMods: StatModifiers


  def getPhysDamage: Int = Utility.roundDown(stats.strength * statMods.strMod)
  def getPysCritDamage: Int = Utility.roundDown((stats.strength * statMods.strMod)/5 +150)
  def getSpeed: Int = Utility.roundDown((stats.agility * statMods.agiMod)/10)
  def getCritChance: Int = Utility.roundDown((stats.agility * statMods.agiMod)/2)
  def getMagDefence: Int = Utility.roundDown(stats.spirit * statMods.spiMod)
  def getMaxMP: Int = Utility.roundDown((stats.spirit * statMods.spiMod)*5)
  def getMagDamage: Int = Utility.roundDown(stats.intelligence * statMods.intMod)
  def getMagicCritDamage: Int = Utility.roundDown((stats.intelligence * statMods.intMod)/10 +150)
  def getMaxHP: Int = Utility.roundDown((stats.resistance * statMods.hpMod)*10)
  def getPhysDefence: Int = Utility.roundDown(stats.resistance * statMods.resMod)

  var currHP: Int = getMaxHP
  var currMP: Int = getMaxMP

  //Passagli la funzione della mossa, sia essa speciale, attacco base o passare il turno   STRATEGY
  def doMove = ???

  def changeCurrHPMP(stat: String,function: (Int,Int)=>Int , value: Int): Unit = stat.toUpperCase match {
    case "HP" => if (function(currHP, value) > getMaxHP) {
      currHP = getMaxHP
    } else if (function(currHP, value) < 0) {
      currHP = 0
    } else {
      currHP = function(currHP, value)
    }
    case "MP" => if (function(currMP, value) > getMaxMP) {
      currMP = getMaxMP
    } else if (function(currMP, value) < 0) {
      currMP = 0
    } else {
      currMP = function(currMP, value)
    }
  }
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
        def apply(role: String,charName: String,stats: Statistics): Character = role match{
                case "Warrior" => Warrior(charName,stats,StatModsFactory(role))
                case "Thief" => Thief(charName,stats,StatModsFactory(role))
                case "Wizard" => Wizard(charName,stats,StatModsFactory(role))
                case "Healer" => Healer(charName,stats,StatModsFactory(role))
        }

}


