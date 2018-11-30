package model

sealed trait ImmutableAffliction
object ImmutableAffliction {
  case object Stunned extends ImmutableAffliction
  case object Poisoned extends ImmutableAffliction
  case object Berserk extends ImmutableAffliction
  case object Silenced extends ImmutableAffliction
  case object Frozen extends ImmutableAffliction
  case object Asleep extends ImmutableAffliction
  case object Blinded extends ImmutableAffliction
}
