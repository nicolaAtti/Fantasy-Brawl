package model

case class Modifier(name: String, affectsSubStatistic: SubStatistic, delta: Int, roundsDuration: Int)
