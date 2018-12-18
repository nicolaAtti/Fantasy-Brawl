package model

/** Contains all the elements that define a modifier.
  *
  * @param name the unique name of the modifier
  * @param affectsSubStatistic the sub-statistic affected by the modifier
  * @param delta the value to temporarily add/subtract from the affected sub-statistic
  * @param remainingRounds the number of rounds in which the modifier will remain active
  *
  * @author Marco Canducci
  */
case class Modifier(name: String, affectsSubStatistic: SubStatistic, delta: Int, remainingRounds: Int)
