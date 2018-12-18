package utilities

import scala.math._

object Misc {

  /** Rounds down a Double value an converts it to an Int.
    *
    * @param doubleValue the value to convert
    * @return the converted value
    */
  def roundDown(doubleValue: Double): Int = floor(doubleValue).toInt

  /** Rounds up a Double value an converts it to an Int.
    *
    * @param doubleValue the value to convert
    * @return the converted value
    */
  def roundUp(doubleValue: Double): Int = ceil(doubleValue).toInt

  /** Clamps the given value inside the interval [minValue, maxValue].
    *
    * @param value the integer number to clamp
    * @param minValue the minimum admitted integer
    * @param maxValue the maximum admitted integer
    * @return the clamped value
    */
  def clamped(value: Int, minValue: Int, maxValue: Int): Int = minValue max value min maxValue

  /** Tells if an integer number is even.
    *
    * @param number the number
    * @return true if even, false otherwise
    */
  def isEven(number: Int): Boolean = number % 2 == 0

  /** Tells if an integer number is odd.
    *
    * @param number the number
    * @return true if odd, false otherwise
    */
  def isOdd(number: Int): Boolean = !isEven(number)

}
