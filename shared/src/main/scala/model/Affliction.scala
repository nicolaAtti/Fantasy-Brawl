package model

sealed trait Affliction {
  def inhibits(move: Move): Boolean
  def beginTurnAlteration: Option[(Status) => Status]
}

object Affliction {
  case object Stunned extends Affliction {
    override def inhibits(move: Move): Boolean = true
    override val beginTurnAlteration = None
  }

  case object Asleep extends Affliction {
    override def inhibits(move: Move): Boolean = true
    override val beginTurnAlteration = None
  }

  case object Poisoned extends Affliction {
    override def inhibits(move: Move): Boolean = false
    override val beginTurnAlteration =
      Some((s) => s.copy(healthPoints = Math.ceil(s.healthPoints * 0.25f).toInt)) // cannot die from poisoning
  }

  case object Berserk extends Affliction {
    override def inhibits(move: Move): Boolean = move.isInstanceOf[SpecialMove]
    override val beginTurnAlteration = None
  }

  import MoveType._

  case object Silenced extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Spell
    override val beginTurnAlteration = None
  }

  case object Frozen extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Melee
    override val beginTurnAlteration = None
  }

  case object Blinded extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Ranged
    override val beginTurnAlteration = None
  }
}
