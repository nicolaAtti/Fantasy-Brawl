import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit._
import com.spingo.op_rabbit.properties.ReplyTo
import communication.MessageFormat.MyFormat
import config.MessagingSettings
import turnordering.PlayerInfo

import scala.util.{Failure, Success}

object Main extends App {

  final val LogMessage = "Received a new turn ordering request"
  final val OldRequestPrint = "Received an old round request: "

  import communication._

  val rabbitControl = ActorSystem().actorOf(Props[RabbitControl])
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.nack(requeue = MessagingSettings.Requeue)

  implicit val requestFormat: MyFormat[StartRoundRequest] = MessageFormat.format[StartRoundRequest]
  implicit val responseFormat: MyFormat[StartRoundResponse] = MessageFormat.format[StartRoundResponse]

  import Queues._
  import config.MiscSettings._

  val requestQueue =
    Queue(StartRoundRequestQueue, durable = MessagingSettings.Durable, autoDelete = MessagingSettings.AutoDelete)
  Subscription.run(rabbitControl) {

    import com.spingo.op_rabbit.Directives._
    import turnordering.MongoDbManager._
    channel(qos = MessagingSettings.Qos) {
      consume(requestQueue) {
        (body(as[StartRoundRequest]) & optionalProperty(ReplyTo)) { (request, replyTo) =>
          {
            import config._
            if (MiscSettings.ServicesLog) {
              println(LogMessage)
            }
            getCurrentRound(request.battleId).onComplete {
              case Success(round: Int) if request.round > round =>
                val player =
                  PlayerInfo(request.playerName, request.playerTeamSpeeds, request.battleId, request.round, replyTo.get)
                addPlayerInfo(player).onComplete {
                  case Success(_) =>
                    getPlayerInfo(request.opponentName, request.battleId, request.round).onComplete {
                      case Success(opponent) =>
                        import Helper._
                        val speedsOrdered = (extractTeamSpeeds(opponent) ++ extractTeamSpeeds(player))
                          .sortBy { case (_, speed) => speed }
                          .reverse
                          .map {
                            case (character, _) => character
                          }
                        val response = StartRoundResponse(Right(speedsOrdered), request.round)
                        rabbitControl ! Message.queue(response, replyTo.get)
                        rabbitControl ! Message.queue(response, opponent.replyTo)
                        deletePlayerInfo(request.playerName, request.battleId, request.round)
                        deletePlayerInfo(opponent.name, request.battleId, request.round)
                        incrementCurrentRound(request.battleId)
                      case Failure(e) => println(s"$LogFailurePrefix$e")
                    }
                  case Failure(e) => println(s"$LogFailurePrefix$e")
                }
              case Success(round: Int) => println(s"$OldRequestPrint ${request.round} actual $round")
              case Failure(e)          => println(s"$LogFailurePrefix$e")
            }
            ack
          }
        }
      }
    }
  }

  private object Helper {

    def extractTeamSpeeds(player: PlayerInfo): List[((String, String), Int)] = {
      player.teamSpeeds.map {
        case (characterName, speed) => (player.name, characterName) -> speed
      }.toList
    }
  }
}
