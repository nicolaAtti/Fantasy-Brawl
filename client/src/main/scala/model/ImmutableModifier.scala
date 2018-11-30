package model

case class ImmutableModifier(affectsSubStatistic: SubStatistic, delta: Int, roundsDuration: Int)
