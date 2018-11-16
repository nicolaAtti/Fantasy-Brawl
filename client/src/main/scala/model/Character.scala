package model

/**
  * Default implementation of a game Character
  *
  * @param charName   the name of the Character
  * @param stats      the statistics of the Character
  * @param classMods   the class statistic modifier, this will vary from Character implementation
  * @author Nicola Atti
  */
trait Character{

  var charName: String
  var stats: Statistics
  var classMods: StatModifiers
  var afflictions: List[Affliction] = List()
  var statMods: List[Modifier] = List()
  import utilities.Utility._

  //Mi immagino che questi metodi dovranno controllare anche evenutali modificatori o afflizioni dovuti alle mosse
  def getPhysDamage: Int = roundDown(stats.strength * classMods.strMod)
  def getPysCritDamage: Int = roundDown((stats.strength * classMods.strMod)/5 +150)
  def getSpeed: Int = roundDown((stats.agility * classMods.agiMod)/10)
  def getCritChance: Int = roundDown((stats.agility * classMods.agiMod)/2)
  def getMagDefence: Int = roundDown(stats.spirit * classMods.spiMod)
  def getMaxMP: Int = roundDown((stats.spirit * classMods.spiMod)*5)
  def getMagDamage: Int = roundDown(stats.intelligence * classMods.intMod)
  def getMagicCritDamage: Int = roundDown((stats.intelligence * classMods.intMod)/10 +150)
  def getMaxHP: Int = roundDown((stats.resistance * classMods.hpMod)*10)
  def getPhysDefence: Int = roundDown(stats.resistance * classMods.resMod)

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


private case class Warrior(override var charName: String, override var stats: Statistics, override var classMods: StatModifiers = StatModifiers(2,1,1,0.5,1.5,2)) extends Character {
}
private case class Thief(override var charName: String, override var stats: Statistics, override var classMods: StatModifiers = StatModifiers(1.5,2,1,0.5,1,1.5)) extends Character {
}
private case class Wizard(override var charName: String, override var stats: Statistics, override var classMods: StatModifiers = StatModifiers(1,1,1.5,2,0.5,1.5)) extends Character {
}
private case class Healer(override var charName: String,override var stats: Statistics, override var classMods: StatModifiers = StatModifiers(1.5,0.5,1,2,1,1.5)) extends Character {
}


object Character{
        def apply(role: String,charName: String,stats: Statistics): Character = role match{
                case "Warrior" => Warrior(charName,stats)
                case "Thief" => Thief(charName,stats)
                case "Wizard" => Wizard(charName,stats)
                case "Healer" => Healer(charName,stats)
        }

}
