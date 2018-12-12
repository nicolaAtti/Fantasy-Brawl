package model

sealed trait Alteration {
  def inhibits(move: Move): Boolean
  def turnDuration: Int
  def beginTurnStatusVariation: Option[(Status) => Status]
  val acronym: String
}

object Alteration {

  val poisonedMultiplier = 0.75
  val regenerationMultiplier = 1.25

  case object Stunned extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 1
    override val beginTurnStatusVariation = None
    override val acronym: String = "Stn"
  }

  case object Asleep extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym: String = "Slp"
  }

  case object Poisoned extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 3
    override val beginTurnStatusVariation =
      Some(s => s.copy(healthPoints = Math.ceil(s.healthPoints * poisonedMultiplier).toInt)) // cannot die from poisoning
    override val acronym: String = "Psn"
  }

  case object Regeneration extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 2
    override val beginTurnStatusVariation: Option[Status => Status] =
      Some(s => s.copy(healthPoints = Math.round(s.healthPoints * regenerationMultiplier).toInt min s.maxHealthPoints))
    override val acronym: String = "Reg"
  }

  case object Berserk extends Alteration {
    override def inhibits(move: Move) = move.isInstanceOf[SpecialMove]
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
    override val acronym: String = "Brk"
  }

  import MoveType._

  case object Silenced extends Alteration {
    override def inhibits(move: Move) = move.moveType == Spell
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Sil"
  }

  case object Frozen extends Alteration {
    override def inhibits(move: Move) = move.moveType == Melee
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Frz"
  }

  case object Blinded extends Alteration {
    override def inhibits(move: Move) = move.moveType == Ranged
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
    override val acronym: String = "Bln"
  }

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

}
