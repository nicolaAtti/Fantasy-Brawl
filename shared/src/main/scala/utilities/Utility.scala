package utilities

import scala.math._

object Utility {

  /**
    * Rounds down a Double value an converts it to an Int
    *
    * @param doubleValue the value to convert
    * @return the converted value
    */
  def roundDown(doubleValue: Double): Int = floor(doubleValue) toInt

  /**
    * Rounds up a Double value an converts it to an Int
    *
    * @param doubleValue the value to convert
    * @return the converted value
    */
  def roundUp(doubleValue: Double): Int = ceil(doubleValue) toInt

}
