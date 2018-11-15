package model
/**
  * Contains the class modifiers to the Character statistics, based on the Character's class
  *
  * @param strMod
  * @param agiMod
  * @param spiMod
  * @param intMod
  * @param resMod
  * @param hpMod
  * @author Nicola Atti
  */
case class StatModifiers(strMod: Double, agiMod: Double, spiMod: Double, intMod: Double, resMod: Double, hpMod: Double){
}

object StatModsFactory{
  def apply(role: String): StatModifiers = role match {
    case "Warrior" => StatModifiers(2,1,1,0.5,1.5,2)
    case "Thief" => StatModifiers(1.5,2,1,0.5,1,1.5)
    case "Wizard" => StatModifiers(1,1,1.5,2,0.5,1.5)
    case "Healer" => StatModifiers(1.5,0.5,1,2,1,1.5)
  }
}



