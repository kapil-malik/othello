package com.kmalik.othello.lib.types

import com.kmalik.othello.lib.types.Board._

trait GameAuditor {
  
  def onGameStart(b:Board, p1: Player, p2: Player):Unit
  
  def afterGetPossibleMoves(b:Board, p:Player, ms:Seq[Position]):Unit
  
  def afterGetMove(b:Board, p:Player, ms:Seq[Position], m:Position):Unit
  
  def afterMakeMove(b:Board, p:Player, m:Position):Unit
    
  def onGameEnd(b:Board, p1: Player, p2: Player):Unit
  
}