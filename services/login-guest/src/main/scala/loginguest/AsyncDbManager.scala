package loginguest

import scala.concurrent.Future

/** Provides functionality to manage the asynchronous interaction between the
  * login service and a database.
  *
  * @author Marco Canducci
  */
trait AsyncDbManager {

  /** Atomically returns and updates (increasing it by one) the guests counter.
    *
    * @return a Future with the unique guest number
    */
  def nextGuestNumber: Future[Int]
}
