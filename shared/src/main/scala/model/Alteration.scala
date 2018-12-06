package model

sealed trait Alteration {
  def inhibits(move: Move): Boolean
  def beginTurnStatusVariation: Option[(Status) => Status]
}

object Alteration {

  val poisonedMultiplier = 0.75
  val regenerationMultiplier = 1.25

  case object Stunned extends Alteration {
    override def inhibits(move: Move): Boolean = true
    override val beginTurnStatusVariation = None
  }

  case object Asleep extends Alteration {
    override def inhibits(move: Move): Boolean = true
    override val beginTurnStatusVariation = None
  }

  case object Poisoned extends Alteration {
    override def inhibits(move: Move): Boolean = false
    override val beginTurnStatusVariation =
      Some(s => s.copy(healthPoints = Math.ceil(s.healthPoints * poisonedMultiplier).toInt)) // cannot die from poisoning
  }

  case object Regeneration extends Alteration {
    override def inhibits(move: Move): Boolean = false
    override val beginTurnStatusVariation: Option[Status => Status] =
      Some(s => s.copy(healthPoints = Math.round(s.healthPoints * regenerationMultiplier).toInt min s.maxHealthPoints))
  }

  case object Berserk extends Alteration {
    override def inhibits(move: Move): Boolean = move.isInstanceOf[SpecialMove]
    override val beginTurnStatusVariation = None
  }

  import MoveType._

  case object Silenced extends Alteration {
    override def inhibits(move: Move): Boolean = move.moveType == Spell
    override val beginTurnStatusVariation = None
  }

  case object Frozen extends Alteration {
    override def inhibits(move: Move): Boolean = move.moveType == Melee
    override val beginTurnStatusVariation = None
  }

  case object Blinded extends Alteration {
    override def inhibits(move: Move): Boolean = move.moveType == Ranged
    override val beginTurnStatusVariation = None
  }

}
