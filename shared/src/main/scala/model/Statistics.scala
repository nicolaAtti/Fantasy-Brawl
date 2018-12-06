package model

/**
  * Contains the main statistics of a Character
  *
  * @param strength     dictates the strength of the character
  * @param agility      dictates the agility of the character
  * @param spirit       dictates the character's force of will
  * @param intelligence dictates the magic capabilities of a character
  * @param resistance   dictates how much a character is tough
  * @author Nicola Atti
  */
case class Statistics(strength: Int, agility: Int, spirit: Int, intelligence: Int, resistance: Int)
