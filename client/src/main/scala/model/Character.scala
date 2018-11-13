package main.scala.model

import main.resources.Utils

trait Character{

        var charName: String

        var strength: Int
        var agility: Int
        var spirit: Int
        var intelligence: Int
        var resistance: Int

        var strengthMod: Double
        var agilityMod: Double
        var spiritMod: Double
        var intelligenceMod: Double
        var resistanceMod: Double
        var healthMod: Double

        var physDamage: Int = Utils.roundDown(strength*strengthMod)
        var critPhysDamage: Int = Utils.roundDown((strength*strengthMod)/5 + 150)
        var speed: Int = Utils.roundDown((agility*agilityMod)/10)
        var critChance: Double = (agility*agilityMod)/2
        var magicDefence: Int = Utils.roundDown(spirit*spiritMod)
        var maxMP: Int = Utils.roundDown((spirit*spiritMod)*5)
        var magicDamage: Int = Utils.roundDown(intelligence*intelligenceMod)
        var magicCritDamage: Int = Utils.roundDown((intelligence*intelligenceMod)/5 +150)
        var healPotency: Int = Utils.roundDown(intelligence*intelligenceMod)
        var healCritPotency: Int = Utils.roundDown((intelligence*intelligenceMod)/5 +150)
        var maxHP: Int = Utils.roundDown((resistance*healthMod)*10)
        var physDefence: Int = Utils.roundDown(resistance*resistanceMod)

        var currHP: Int = maxHP
        var currMP: Int = maxMP

        def changeCurrHPMP(stat: String,function: (Int,Int)=>Int , value: Int): Unit = stat match {
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
        }

        def basicAttack = ???
        def doSpecialMove = ???
}


private case class Warrior(charName: String,strength:Int,agility:Int,spirit: Int,intelligence: Int, resistance: Int) extends Character {
        override var strengthMod: Double = 2
        override var agilityMod: Double = 1
        override var spiritMod: Double = 1
        override var intelligenceMod: Double = 0.5
        override var resistanceMod: Double = 1.5
        override var healthMod: Double = 2
}

private case class Thief(charName: String,strength:Int,agility:Int,spirit: Int,intelligence: Int, resistance: Int) extends Character {
        override var strengthMod: Double = 1.5
        override var agilityMod: Double = 2
        override var spiritMod: Double = 1
        override var intelligenceMod: Double = 0.5
        override var resistanceMod: Double = 1
        override var healthMod: Double = 1.5
}
private case class Wizard(charName: String,strength:Int,agility:Int,spirit: Int,intelligence: Int, resistance: Int) extends Character {
        override var strengthMod: Double = 1
        override var agilityMod: Double = 1
        override var spiritMod: Double = 1.5
        override var intelligenceMod: Double = 2
        override var resistanceMod: Double = 0.5
        override var healthMod: Double = 1.5
}
private case class Healer(charName: String,strength:Int,agility:Int,spirit: Int,intelligence: Int, resistance: Int) extends Character {
        override var strengthMod: Double = 1.5
        override var agilityMod: Double = 0.5
        override var spiritMod: Double = 1
        override var intelligenceMod: Double = 2
        override var resistanceMod: Double = 1
        override var healthMod: Double = 1.5
}

//Companion object
object CharacterFactory{
        def apply(role: String,charName: String,strenght: Int,agility: Int,spirit: Int,intelligence: Int,resistance: Int): Character = role match{
                case "Warrior" => new Warrior(charName,strenght,agility,spirit,intelligence,resistance)
                case "Thief" => new Thief(charName,strenght, agility, spirit, intelligence, resistance)
                case "Wizard" => new Wizard(charName,strenght,agility,spirit,intelligence,resistance)
                case "Healer" => new Healer(charName,strenght,agility,spirit,intelligence,resistance)
        }

}
