package model

sealed trait Affliction {
  def inhibits(move: Move): Boolean
  def endingRoundAlteration: Option[StatusAlteration]
}

object Affliction {
  case object Stunned extends Affliction {
    override def inhibits(move: Move): Boolean = true
    override val endingRoundAlteration = None
  }

  case object Asleep extends Affliction {
    override def inhibits(move: Move): Boolean = true
    override val endingRoundAlteration = None
  }

  case object Poisoned extends Affliction {
    override def inhibits(move: Move): Boolean = false
    override val endingRoundAlteration = Some(StatusAlteration(hp => Math.round(hp * 0.25f), mp => mp, Map(), Map()))
  }

  case object Berserk extends Affliction {
    override def inhibits(move: Move): Boolean = move.isInstanceOf[SpecialMove]
    override val endingRoundAlteration = None
  }

  import model.MoveType._

  case object Silenced extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Spell
    override val endingRoundAlteration = None
  }

  case object Frozen extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Melee
    override val endingRoundAlteration = None
  }

  case object Blinded extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Ranged
    override val endingRoundAlteration = None
  }
}
