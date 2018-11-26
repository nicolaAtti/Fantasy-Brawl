package controller

import alice.tuprolog._

import scala.io.Source

object ScalaProlog extends App{
  val engine = new Prolog
  engine.setTheory(new Theory("character('Jacob','Warrior',51,33,13,5,27,['Skullcrack','Sismic Slam','Berserker Rage','Second Wind']).\ncharacter('Annabelle','Thief',34,46,22,8,27,['Poison Vial','Flash Grenade','Preparation','Backstab']).\ncharacter('Albert','Healer',7,22,34,58,23,['Lay on Hands','Holy Smite','Censure','Bolster Faith']).\ncharacter('Lidya','Wizard',19,11,33,54,27,['Flamestrike','Hypnosis','Freezing Wind','Concentration'])."))
  var solveInfo: SolveInfo = engine.solve("character(A,B,C,D,E,F,G,H).")
  while(solveInfo.isSuccess ){
    println(solveInfo.getSolution)
    println(solveInfo.getVarValue("A"))
    if(engine.hasOpenAlternatives){
      solveInfo = engine.solveNext()
    }else{
    }
    //Source.fromFile("C:\\Users\\UTENTE\\IdeaProjects\\pps-17-fb\\client\\src\\main\\resources\\model\\PrologCharacters.pl").toString()
  }

}
