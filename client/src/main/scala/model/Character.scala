package model

trait Character {

  /**
    * The character's name
    */
  var charName: String

  /**
    * The character's base statistics
    */
  var stats: Statistics

  /**
    * The character's class multipliers
    */
  var classMods: ClassStat

  import utilities.Utility._

  /**
    * The character's maximum HP
    */
  val maxHP: Int = roundDown((stats.resistance * classMods.hpMod) * 10)

  /**
    * The character's maximum MP
    */
  val maxMP: Int = roundDown((stats.spirit * classMods.spiMod) * 5)

  import scala.collection.mutable._

  /**
    * Contains the character's status, depending on current HP and MP values and existing modifiers and afflictions
    */
  var status: Status = Status(maxHP, maxMP, MutableList(), MutableList())

  /**
    * Calculates the character's physical damage
    * @return
    */
  def calculatePhysDamage: Int = roundDown(stats.strength * classMods.strMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus physical critical strike damage
    * @return
    */
  def calculatePysCritDamage: Int =
    roundDown((stats.strength * classMods.strMod) / 5 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's speed
    * @return
    */
  def calculateSpeed: Int = roundDown((stats.agility * classMods.agiMod) / 10) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's critical strike chance
    * @return
    */
  def calculateCritChance: Int = roundDown((stats.agility * classMods.agiMod) / 2) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical defence
    * @return
    */
  def calculateMagDefence: Int = roundDown(stats.spirit * classMods.spiMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's magical damage
    * @return
    */
  def calculateMagDamage: Int = roundDown(stats.intelligence * classMods.intMod) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's bonus magical critical strike damage
    * @return
    */
  def calculateMagicCritDamage: Int =
    roundDown((stats.intelligence * classMods.intMod) / 10 + 150) + getModifierValues("PHYS_DAMAGE")

  /**
    * Calculates the character's physical defence
    * @return
    */
  def calculatePhysDefence: Int = roundDown(stats.resistance * classMods.resMod) + getModifierValues("PHYS_DAMAGE")

  //Passagli la funzione della mossa, sia essa speciale, attacco base o passare il turno   STRATEGY
  //Forse conviene mettere qui il controllo per le afflizzioni che limitano le mosse tipo: if stunnato passa solo, if ghiacciato no mosse meelee etc
  /// TODO:
  def doMove = ???

  /**
    * Resets the status of the character to the original values
    */
  def resetStatus(): Unit = {
    status = Status(maxHP, maxMP, MutableList(), MutableList())
  }

  /**
    * Cycles all the character's modifiers for a given sub-statistic and calculates the bonus/malus value
    * @param affectedStat the sub-statistic to search for
    * @return the total bonus/malus to the sub-statistic
    * @author Nicola Atti
    */
  def getModifierValues(affectedStat: String): Int = {
    var allModsValue = 0
    status.modifiers foreach (modifier => allModsValue = add(allModsValue, modifier.modValue))
    allModsValue
  }

}

/**
  * Default implementation of a game Character
  *
  * @param charName  the name of the Character
  * @param stats     the statistics of the Character
  * @param classMods the class statistic modifier, this will vary from Character implementation
  * @author Nicola Atti
  */
private case class Warrior(override var charName: String,
                           override var stats: Statistics,
                           override var classMods: ClassStat = ClassStat(2, 1, 1, 0.5, 1.5, 2))
    extends Character {}

private case class Thief(override var charName: String,
                         override var stats: Statistics,
                         override var classMods: ClassStat = ClassStat(1.5, 2, 1, 0.5, 1, 1.5))
    extends Character {}

private case class Wizard(override var charName: String,
                          override var stats: Statistics,
                          override var classMods: ClassStat = ClassStat(1, 1, 1.5, 2, 0.5, 1.5))
    extends Character {}

private case class Healer(override var charName: String,
                          override var stats: Statistics,
                          override var classMods: ClassStat = ClassStat(1.5, 0.5, 1, 2, 1, 1.5))
    extends Character {}

object Character {

  def apply(role: String, charName: String, stats: Statistics): Character =
    role match {
      case "Warrior" => Warrior(charName, stats)
      case "Thief"   => Thief(charName, stats)
      case "Wizard"  => Wizard(charName, stats)
      case "Healer"  => Healer(charName, stats)
    }

}
