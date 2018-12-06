package model

sealed trait Alteration {
  def inhibits(move: Move): Boolean
  def turnDuration: Int
  def beginTurnStatusVariation: Option[(Status) => Status]
}

object Alteration {

  val poisonedMultiplier = 0.75
  val regenerationMultiplier = 1.25

  case object Stunned extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 1
    override val beginTurnStatusVariation = None
  }

  case object Asleep extends Alteration {
    override def inhibits(move: Move) = true
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
  }

  case object Poisoned extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 3
    override val beginTurnStatusVariation =
      Some(s => s.copy(healthPoints = Math.ceil(s.healthPoints * poisonedMultiplier).toInt)) // cannot die from poisoning
  }

  case object Regeneration extends Alteration {
    override def inhibits(move: Move) = false
    override val turnDuration = 2
    override val beginTurnStatusVariation: Option[Status => Status] =
      Some(s => s.copy(healthPoints = Math.round(s.healthPoints * regenerationMultiplier).toInt min s.maxHealthPoints))
  }

  case object Berserk extends Alteration {
    override def inhibits(move: Move) = move.isInstanceOf[SpecialMove]
    override val turnDuration = 3
    override val beginTurnStatusVariation = None
  }

  import MoveType._

  case object Silenced extends Alteration {
    override def inhibits(move: Move) = move.moveType == Spell
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
  }

  case object Frozen extends Alteration {
    override def inhibits(move: Move) = move.moveType == Melee
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
  }

  case object Blinded extends Alteration {
    override def inhibits(move: Move) = move.moveType == Ranged
    override val turnDuration = 2
    override val beginTurnStatusVariation = None
  }

}
