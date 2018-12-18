package model

import Move._
import MoveType._
import MoveEffectStrategies._
import model.Alteration._

/** A move is described by its type, the amount of mana points that it costs, the
  * maximum targets that it can be applied to, and its move effect: the move effect
  * will define the new status of the target character hit by the move.
  *
  * @author Marco Canducci
  */
sealed trait Move {
  def moveType: MoveType
  def moveEffect: (Attacker, Target) => NewTargetStatus
  def manaCost: Int
  def maxTargets: Int
}

object Move {
  import MoveHelper._

  type Attacker = Character
  type Target = Character
  type NewTargetStatus = Status
  type NewStatuses = Map[Character, Status]

  /** Tells if a character can or cannot make a move.
    *
    * @param attacker the character that wants to make a move
    * @param move the move
    * @return true if the attacker can make the move, false otherwise
    */
  def canMakeMove(attacker: Attacker, move: Move): Boolean =
    hasEnoughMana(attacker, move) && !isInhibited(attacker, move)

  /** Provides the new status of every character involved in the move execution.
    *
    * A move will affect all the targets (evaluating the move effect for everyone
    * of them) and possibly the attacker if the move costs some mana points.
    * It's worth mentioning that a character can be both attacker and target at
    * the same time: useful for example if a he/she wants to heal himself/herself
    * casting a healing spell.
    *
    * @param move the move to be executed
    * @param attacker the character that makes the move
    * @param targets the character/s that undergo the move effects
    * @return the new status for each character involved
    */
  def makeMove(move: Move, attacker: Attacker, targets: Set[Target]): NewStatuses = {
    val results: NewStatuses =
      targets
        .map(target => target -> move.moveEffect(attacker, target))
        .toMap

    import Status._
    if (move.manaCost > 0 && results.contains(attacker))
      adjust(map = results, key = attacker)(afterManaConsumption(_, move))
    else if (move.manaCost > 0)
      results + (attacker -> afterManaConsumption(attacker.status, move))
    else
      results
  }

  private object MoveHelper {

    /** Denotes if a character currently has enough mana to execute a move.
      *
      * @param character the character that wants to make a move
      * @param move the move
      * @return true if the character has enough mana, false otherwise
      */
    def hasEnoughMana(character: Attacker, move: Move): Boolean = character.status.manaPoints >= move.manaCost

    /** Denotes if a character is currently affected by an alteration that
      * inhibits a certain move.
      *
      * @param character the character that wants to make a move
      * @param move the move
      * @return true if the character is inhibited, false otherwise
      */
    def isInhibited(character: Attacker, move: Move): Boolean =
      character.status.alterations
        .map { case (alteration, _) => alteration.inhibits(move) }
        .fold(false)(_ || _)

    /** Evaluates a new Map, given: a Map, a key contained inside that map, and
      * a function. The new map will contain the updated value for the given key,
      * obtained applying the given function to the corresponding old value.
      *
      * @param map the old map
      * @param key a key value contained inside the map
      * @param f the transformation function
      * @tparam A the key type
      * @tparam B the value type
      * @return the new, updated map
      */
    def adjust[A, B](map: Map[A, B], key: A)(f: B => B): Map[A, B] = map.updated(key, f(map(key)))

  }

}

/** The basic move, practicable by every character.
  *
  * It is of type Melee, it doesn't cost any mana points and can hit only one
  * target at a time. Its move effect is a standard damage with zero base damage.
  */
case object PhysicalAttack extends Move {
  override val moveType: MoveType = Melee
  override val moveEffect: (Attacker, Target) => NewTargetStatus =
    createStandardDamageEffect(moveType = this.moveType, baseDamage = 0)(addModifiers = Map.empty,
                                                                         addAlterations = Map.empty,
                                                                         removeAlterations = Set(Asleep))
  override val manaCost = 0
  override val maxTargets = 1
}

/** A special move.
  *
  * @param name the move name
  * @param moveType the move type
  * @param moveEffect the move effect
  * @param manaCost how many mana points the attacker must spend to make the move
  * @param maxTargets how many targets can be hit at once
  */
case class SpecialMove(name: String,
                       moveType: MoveType,
                       moveEffect: (Attacker, Target) => NewTargetStatus,
                       manaCost: Int,
                       maxTargets: Int)
    extends Move {

  require(manaCost >= 0, "The mana cost cannot be negative")
  require(maxTargets > 0, "The number of maximum targets must be at least one")
}
