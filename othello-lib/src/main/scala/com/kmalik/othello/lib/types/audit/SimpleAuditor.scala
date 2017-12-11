package com.kmalik.othello.lib.types.audit

import com.kmalik.othello.lib.types.GameAuditor
import com.kmalik.othello.lib.types.Board._
import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Player

class SimpleAuditor(private val pace:Int = 1) extends GameAuditor {
  
  def onGameStart(b:Board, p1: Player, p2: Player):Unit = {
    println("Game Start")
    println(s"${toStr(b)}")
    println(s"Player1: ${toStr(p1)}, Player2: ${toStr(p2)}")    
  }
  
  def afterGetPossibleMoves(b:Board, p:Player, ms:Seq[Position]):Unit = {
    Thread.sleep(pace * 500L)
    println(s"Possible Moves for ${toStr(p)}: ${ms.mkString(";")}") 
  }
  
  def afterGetMove(b:Board, p:Player, ms:Seq[Position], m:Position):Unit = {
    Thread.sleep(pace * 500L)
    println(s"Selected Move : $m")    
  }
  
  def afterMakeMove(b:Board, p:Player, m:Position):Unit = {
    Thread.sleep(pace * 1000L)
    println(s"${toStr(b)}")
  }
    
  def onGameEnd(b:Board, p1: Player, p2: Player):Unit = {
    Thread.sleep(pace * 1000L)
    val scores = b.getCounts()
    println(s"Scores : ${toStr(WHITE)}=${scores._1}, ${toStr(BLACK)}=${scores._2}")
    println("Game Over")
  }
  
  private def toStr(b:Board):String = {
    val str = b.serialize()
               .replaceAll(EMPTY.toString,toStr(EMPTY))
               .replaceAll(BLACK.toString,toStr(BLACK))
               .replaceAll(WHITE.toString,toStr(WHITE))
               
    (0 to 7).map(i => str.substring(8*i, 8*i + 8)).mkString("\n")
  }
  
  private def toStr(p:Player):String = {
    s"${p.name} [${toStr(p.color)}]"
  }
  
  private def toStr(c:Color):String = {    
    if (c == WHITE)  "W"
    else if (c == BLACK) "B"
    else "_"
  }
    
}

object SimpleAuditor {
  def apply(pace:Int = 1) = new SimpleAuditor(pace)
}