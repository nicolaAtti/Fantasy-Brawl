package model

/**
  * Contains the class modifiers to the Character statistics, based on the Character's class
  *
  * @param strMod the class modifier impacting strength driven statistics
  * @param agiMod the class modifier impacting agility driven statistics
  * @param spiMod the class modifier impacting spirit driven statistics
  * @param intMod the class modifier impacting intelligence driven statistics
  * @param resMod the class modifier impacting resistence driven statistics
  * @param hpMod the class modifier impacting maximum HP values
  * @author Nicola Atti
  */
case class StatModifiers(strMod: Double,
                         agiMod: Double,
                         spiMod: Double,
                         intMod: Double,
                         resMod: Double,
                         hpMod: Double) {}
