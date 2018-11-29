package model

sealed trait Move {
  def baseDamage: Int
  def moveType: MoveType
}

case class PhysicalAttack(baseDamage: Int) extends Move {
  override val moveType = MoveType.Melee
}

case class SpecialMove(name: String,
                       manaCost: Int,
                       baseDamage: Int,
                       moveType: MoveType,
                       causesAfflictions: Set[Affliction],
                       causesModifiers: Set[Modifier],
                       nTargets: Int)
  extends Move

object Move {

  def canMakeMove(move: Move, whoWantsToMakeIt: Character): Boolean = ???

  def makeMove(move: Move, whoMakesIt: Character, Targets: List[Character]): MoveEffect = ???

}
