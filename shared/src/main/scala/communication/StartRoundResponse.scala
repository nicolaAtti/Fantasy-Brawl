package communication

import communication.StatusUpdateMessage.CharacterKey

/** The response message that the turn ordering service sends to the client to
  * inform them about the next round ordered turns.
  *
  * @param turnInformation Right(List(Character) ordered turns
  *                        Left(error) details about the error
  * @param round the round that's about to start
  */
case class StartRoundResponse(turnInformation: Either[String, List[CharacterKey]], round: Int)
