package model

sealed trait MoveType

object MoveType {
  case object Melee extends MoveType
  case object Ranged extends MoveType
  case object Spell extends MoveType
}
