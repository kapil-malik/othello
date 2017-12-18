package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board.Color
import com.kmalik.othello.lib.types.Board.Position

class RandomMaxFlipsAlgo extends OthelloAlgo {
  
  def select(b:Board, c:Color, ms:Seq[Position]):Position = {    
    val sortedFlipCounts = ms.map(p => (p, b.getMoveFlipCount(c, p))).sortBy(-_._2)
    val maxValue = sortedFlipCounts(0)._2
    val maxValueMoves = sortedFlipCounts.filter(_._2 == maxValue).map(_._1)
    val index = (maxValueMoves.size * Math.random()).toInt
    maxValueMoves(index)
  }
}

object RandomMaxFlipsAlgo {
  def apply() = new RandomMaxFlipsAlgo()
}