package model

/** An alteration is something that affects a character's behavior for a certain
  * amount of rounds.
  *
  * An alteration can inhibit some kind of moves, or even modify the status of
  * the affected character at the beginning of its turn (describing for example
  * how many health points it will gain or lose).
  */
sealed trait Alteration {

  /** Denotes if a certain move is inhibited by the alteration.
    *
    * @param move the move to test
    * @return true if the move is inhibited, false otherwise
    */
  def inhibits(move: Move): Boolean

  /** Indicates how many rounds the alteration will last. */
  def roundsDuration: Int

  /** The status variation to apply at the beginning of the affected character's turn */
  def beginTurnStatusVariation: Option[(Status) => Status]

  /** The acronym used to represent the alteration. */
  def acronym: String
}

object Alteration {

  val poisonedMultiplier = 0.75
  val regenerationMultiplier = 1.25

  /** When a character is stunned it cannot make any move for one turn. */
  case object Stunned extends Alteration {
    override def inhibits(move: Move) = true
    override val roundsDuration = 1
    override val beginTurnStatusVariation = None
    override val acronym = "Stn"
  }

  /** When a character is asleep it cannot make any move. This status lasts up to
    * three rounds if the character is not awakened first by other characters.
    */
  case object Asleep extends Alteration {
    override def inhibits(move: Move) = true
    override val roundsDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym = "Slp"
  }

  /** When a character is poisoned it must lose a quarter of its health points
    * for three rounds in a row.
    */
  case object Poisoned extends Alteration {
    override def inhibits(move: Move) = false
    override val roundsDuration = 3
    override val beginTurnStatusVariation =
      Some(s => s.copy(healthPoints = Math.ceil(s.healthPoints * poisonedMultiplier).toInt)) // cannot die from poisoning
    override val acronym = "Psn"
  }

  /** When a character is under the regeneration effect it must gain a quarter of
    * its life for two rounds in a row.
    */
  case object Regeneration extends Alteration {
    override def inhibits(move: Move) = false
    override val roundsDuration = 2
    override val beginTurnStatusVariation: Option[Status => Status] =
      Some(s => s.copy(healthPoints = Math.round(s.healthPoints * regenerationMultiplier).toInt min s.maxHealthPoints))
    override val acronym = "Reg"
  }

  /** A character gone berserk cannot make any special move for three rounds. */
  case object Berserk extends Alteration {
    override def inhibits(move: Move) = move.isInstanceOf[SpecialMove]
    override val roundsDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym = "Brk"
  }

  import MoveType._

  /** A silenced character cannot cast any spell for two rounds. */
  case object Silenced extends Alteration {
    override def inhibits(move: Move) = move.moveType == Spell
    override val roundsDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym = "Sil"
  }

  /** A frozen character cannot make any melee move for two rounds. */
  case object Frozen extends Alteration {
    override def inhibits(move: Move) = move.moveType == Melee
    override val roundsDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym = "Frz"
  }

  /** A blinded character cannot make any ranged move for two rounds. */
  case object Blinded extends Alteration {
    override def inhibits(move: Move) = move.moveType == Ranged
    override val roundsDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym = "Bln"
  }

  /** Retrieves the appropriate alteration object given its acronym.
    *
    * @param acronym the alteration's acronym
    * @return the corresponding alteration object
    */
  def apply(acronym: String): Alteration = acronym match {
    case Stunned.acronym      => Stunned
    case Asleep.acronym       => Asleep
    case Poisoned.acronym     => Poisoned
    case Regeneration.acronym => Regeneration
    case Berserk.acronym      => Berserk
    case Silenced.acronym     => Silenced
    case Frozen.acronym       => Frozen
    case Blinded.acronym      => Blinded
    case _                    => throw new IllegalArgumentException(s"Unknown alteration acronym: $acronym")
  }

}
