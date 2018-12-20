package model

/** Contains all the elements that define a modifier.
  *
  * @param name the unique name of the modifier
  * @param affectsSubStatistic the sub-statistic affected by the modifier
  * @param delta the value to temporarily add/subtract from the affected sub-statistic
  * @param roundsDuration how many rounds the alteration will last.
  *
  * @author Marco Canducci
  */
case class Modifier(name: String, affectsSubStatistic: SubStatistic, delta: Int, roundsDuration: Int)
