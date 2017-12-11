package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._

class RandomMoveAlgo extends OthelloAlgo {
  
  def select(b:Board, c:Color, ms:Seq[Position]):Position = {
    val index = (ms.size * Math.random()).toInt
    ms(index)
  }
}

object RandomMoveAlgo {
  def apply() = new RandomMoveAlgo()
}