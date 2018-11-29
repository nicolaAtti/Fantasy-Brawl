package model

case class ImmutableModifier(modId: String, affectedStat: String, turnDuration: Int, modValue: Int)

object ImmutableModifier {

  def ticked(m: ImmutableModifier): ImmutableModifier = m.copy(turnDuration = m.turnDuration - 1)

  def ticked(ml: List[ImmutableModifier]): List[ImmutableModifier] = ml.map(ticked).filter(_.turnDuration > 0)

}
