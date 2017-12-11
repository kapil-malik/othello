package com.kmalik.othello.lib.types

import com.kmalik.othello.lib.algo.OthelloAlgo
import com.kmalik.othello.lib.types.Board._

case class Player(
  val name:String,
  val color:Color,
  val algo:OthelloAlgo) {
  
  def getMove(b:Board, moves:Seq[Position]):Position = {
    algo.select(b, color, moves)
  }
  
}