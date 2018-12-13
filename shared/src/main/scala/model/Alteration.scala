package model

/** An Alteration is something that affects a character's behavior for a certain
  * amount of turns.
  *
  * It particular, every alteration describes:
  * 1) What type moves it inhibits.
  * 2) How to alter the status of an affected character at the beginning of its
  * turn (for example how much its health points will be augmented or diminished).
  */
sealed trait Alteration {
  def inhibits(move: Move): Boolean
  def turnDuration: Int
  def beginTurnStatusVariation: Option[(Status) => Status]
  def acronym: String
}

object Alteration {

  val poisonedMultiplier = 0.75
  val regenerationMultiplier = 1.25

  /** Retrieves the appropriate Alteration object given its string representation.
    *
    * @param name the name of the alteration
    * @return the appropriate alteration object
    */
  def apply(name: String): Alteration = name match {
    case "Stunned"      => Stunned
    case "Asleep"       => Asleep
    case "Poisoned"     => Poisoned
    case "Regeneration" => Regeneration
    case "Berserk"      => Berserk
    case "Silenced"     => Silenced
    case "Frozen"       => Frozen
    case "Blinded"      => Blinded
    case _              => throw new IllegalArgumentException(s"Unknown alteration: $name")
  }

  /** When a character is stunned it cannot make any move for one turn. */
  case object Stunned extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 1
    override val beginTurnStatusVariation = None
    override val acronym: String = "Stn"
  }

  /** When a character is asleep it cannot make any move. This status lasts up to
    * three turns if the character is not awakened first by other characters.
    */
  case object Asleep extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym: String = "Slp"
  }

  /** When a character is poisoned it must lose a quarter of its health points
    * for three turns in a row.
    */
  case object Poisoned extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 3
    override val beginTurnStatusVariation =
      Some(s => s.copy(healthPoints = Math.ceil(s.healthPoints * poisonedMultiplier).toInt)) // cannot die from poisoning
    override val acronym: String = "Psn"
  }

  /** When a character is under the regeneration effect it must gain a quarter of
    * its life for two turns in a row.
    */
  case object Regeneration extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 2
    override val beginTurnStatusVariation: Option[Status => Status] =
      Some(s => s.copy(healthPoints = Math.round(s.healthPoints * regenerationMultiplier).toInt min s.maxHealthPoints))
    override val acronym: String = "Reg"
  }

  /** A character gone berserk cannot make any special move for three turns. */
  case object Berserk extends Alteration {
    override def inhibits(move: Move) = move.isInstanceOf[SpecialMove]
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym: String = "Brk"
  }

  import MoveType._

  /** A silenced character cannot cast any spell for two turns. */
  case object Silenced extends Alteration {
    override def inhibits(move: Move) = move.moveType == Spell
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Sil"
  }

  /** A frozen character cannot make any melee move for two turns. */
  case object Frozen extends Alteration {
    override def inhibits(move: Move) = move.moveType == Melee
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Frz"
  }

  /** A blinded character cannot make any ranged move for two turns. */
  case object Blinded extends Alteration {
    override def inhibits(move: Move) = move.moveType == Ranged
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Bln"
  }
}
