package config

object DbNaming {

  val ClientName = "mongodb://fantasy-brawl-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk"
  val DatabaseName = "heroku_3bppsqjk"

  object Login {
    val CollectionName = "counters"

    val GuestsDocumentId = "guests"
    val GuestsNumber = "number"
  }

  object CasualQueue {
    val CollectionName = "casual-queue"

    val TicketsDocumentId = "casual-queue-ticket"
    val Ticket = "ticket"

    val PlayerName = "playerName"
    val TeamMembers = "teamMembers"
    val ReplyTo = "replyTo"
  }

  object ActiveBattles {
    val CollectionName = "active-battles"

    val CurrentRound = "current-round"
  }

  object TurnOrdering {
    val CollectionName = "turn-ordering-requests"
  }

}
