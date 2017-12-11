package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._

class FirstMoveAlgo extends OthelloAlgo {
  
  def select(b:Board, c:Color, ms:Seq[Position]):Position = ms(0)
}

object FirstMoveAlgo {
  def apply() = new FirstMoveAlgo()
}