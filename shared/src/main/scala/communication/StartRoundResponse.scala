package communication

case class StartRoundResponse(turnInformation: Either[String, List[((String, String), Int)]], round: Int)
