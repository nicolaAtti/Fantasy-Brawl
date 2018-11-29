package model

/**
  * Contains the class modifiers to the Character statistics, based on the Character's class
  *
  * @param strengthMod the class modifier impacting strength driven statistics
  * @param agilityMod the class modifier impacting agility driven statistics
  * @param spiritMod the class modifier impacting spirit driven statistics
  * @param intelligenceMod the class modifier impacting intelligence driven statistics
  * @param resistanceMod the class modifier impacting resistence driven statistics
  * @param hpMod the class modifier impacting maximum HP values
  * @author Nicola Atti
  */
case class ClassStat(strengthMod: Double, agilityMod: Double, spiritMod: Double, intelligenceMod: Double, resistanceMod: Double, hpMod: Double)
