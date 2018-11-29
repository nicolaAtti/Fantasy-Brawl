package model

case class ImmutableAffliction(afflictionType: String, turnDuration: Int)

object ImmutableAffliction {

  def ticked(a: ImmutableAffliction): ImmutableAffliction = a.copy(turnDuration = a.turnDuration - 1)

  def ticked(al: List[ImmutableAffliction]): List[ImmutableAffliction] = al.map(ticked).filter(_.turnDuration > 0)

}
