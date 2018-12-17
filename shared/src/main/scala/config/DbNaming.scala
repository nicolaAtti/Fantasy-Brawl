package config

object DbNaming {

  val ClientName = "mongodb://fantasy-brawl-service:pps-17-fb@ds039291.mlab.com:39291/heroku_3bppsqjk"
  val DatabaseName = "heroku_3bppsqjk"

  object Login {
    val CollectionName = "login"

    val GuestsDocumentId = "guests"
    val GuestNumber = "guest_number"
  }

  object CasualQueue {
    val CollectionName = "casual_queue"

    val TicketsDocumentId = "casual_queue_tickets"
    val TicketNumber = "ticket_number"

    val PlayerName = "player_name"
    val TeamMembers = "team_members"
    val BattleQueue = "battle_queue"
    val ReplyTo = "reply_to"
    val LeftTheQueue = "left_the_queue"
  }

  object ActiveBattles {
    val CollectionName = "active_battles"

    val CurrentRound = "current_round"
  }

  object TurnOrdering {
    val CollectionName = "turn_ordering_requests"

    val PlayerName = "player_id"
    val BattleId = "battle_id"
    val Round = "round_id"
    val TeamNames = "team_names"
    val TeamSpeedValues = "team_speed_values"
    val ReplyTo = "reply_to_queue"
  }

}
