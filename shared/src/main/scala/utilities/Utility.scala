package utilities

import scala.math._

object Utility {

  def roundDown(doubleValue: Double): Int = floor(doubleValue) toInt

  def roundUp(doubleValue: Double): Int = ceil(doubleValue) toInt

  def add(x: Int, y: Int): Int = x + y

  def sub(x: Int, y: Int): Int = x - y
}
