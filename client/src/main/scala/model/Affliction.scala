package model

sealed trait Affliction {
  def inhibits(move: Move): Boolean
//  def afterRoundEffects(status: Status): Status
}

object Affliction {

  case object Stunned extends Affliction {
    override def inhibits(move: Move): Boolean = true
  }

  case object Asleep extends Affliction {
    override def inhibits(move: Move): Boolean = true
  }

  case object Poisoned extends Affliction {
    override def inhibits(move: Move): Boolean = false
  }

  case object Berserk extends Affliction {
    override def inhibits(move: Move): Boolean = move.isInstanceOf[SpecialMove]
  }

  import model.MoveType._

  case object Silenced extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Spell
  }

  case object Frozen extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Melee
  }

  case object Blinded extends Affliction {
    override def inhibits(move: Move): Boolean = move.moveType == Ranged
  }

}
