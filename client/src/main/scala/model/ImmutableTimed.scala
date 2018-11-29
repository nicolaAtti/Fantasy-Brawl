package model

trait ImmutableTimed {
  def turnDuration: Int
  def ticked: ImmutableTimed
//  def withDuration: ImmutableTimed
}

case class ImmutableAffliction(afflictionType: String, turnDuration: Int) extends ImmutableTimed {
  override def ticked = this.copy(turnDuration = this.turnDuration - 1)
}

object ImmutableTimed {

  /**
    * Class implementing afflictons
    * @param afflictionType  defines the type of affliction between the set types (Asleep,Stunned,Frozen,Poisoned,Blinded,Silenced,Berserk)
    * @param turnDuration determines the duration in turns of the affliction, when it reaches 0 it no longer has an effect
    * @author Nicola Atti
    */
//  case class Modifier(modId: String, affectedStat: String, turnDuration: Int, modValue: Int) extends ImmutableTimed = {
//    override def ticked = this.copy
//  }
//
//  def tick(timed: ImmutableTimed): ImmutableTimed = timed match {
//    case Affliction(_, td) => timed.asInstanceOf[Affliction].copy(turnDuration = td - 1)
//    case Modifier(_, _, td, _) => timed.asInstanceOf[Modifier].copy(turnDuration = td - 1)
//  }
//
//  def withDuration(timed: ImmutableTimed, duration: Int): ImmutableTimed = timed match {
//    case Affliction(_, _) => timed.asInstanceOf[Affliction].copy(turnDuration = duration)
//    case Modifier(_, _, _, _) => timed.asInstanceOf[Modifier].copy(turnDuration = duration)
//  }
//
//  def tick(timedList: List[ImmutableTimed]): List[ImmutableTimed] = timedList.map(tick).filter(_.turnDuration > 0)

}
