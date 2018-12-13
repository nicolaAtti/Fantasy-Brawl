package model

/**
  * Contains all the elements that define a modifier
  *
  * @param name the name of the modifier
  * @param affectsSubStatistic the SubStatistic affected by the modifier
  * @param delta the value to add/subtract
  * @param roundsDuration the number of rounds when the modifier is in effect
  */
case class Modifier(name: String, affectsSubStatistic: SubStatistic, delta: Int, roundsDuration: Int)
