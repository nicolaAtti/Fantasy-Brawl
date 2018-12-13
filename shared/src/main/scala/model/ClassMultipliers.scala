package model

/** Contains the class modifiers to the Character statistics, based on the Character's class
  *
  * @param strength the class modifier impacting strength driven statistics
  * @param agility the class modifier impacting agility driven statistics
  * @param spirit the class modifier impacting spirit driven statistics
  * @param intelligence the class modifier impacting intelligence driven statistics
  * @param resistance the class modifier impacting resistence driven statistics
  * @param healthPoints the class modifier impacting maximum HP values
  */
case class ClassMultipliers(strength: Double,
                            agility: Double,
                            spirit: Double,
                            intelligence: Double,
                            resistance: Double,
                            healthPoints: Double)
