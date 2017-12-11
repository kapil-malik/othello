package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board.Color
import com.kmalik.othello.lib.types.Board.Position

class RandomMaxValueAlgo extends OthelloAlgo {
  
  def select(b:Board, c:Color, ms:Seq[Position]):Position = {    
    val sortedMoveValues = ms.map(p => (p, b.getMoveValue(c, p))).sortBy(-_._2)
    val maxValue = sortedMoveValues(0)._2
    val maxValueMoves = sortedMoveValues.filter(_._2 == maxValue).map(_._1)
    val index = (maxValueMoves.size * Math.random()).toInt
    maxValueMoves(index)
  }
}

object RandomMaxValueAlgo {
  def apply() = new RandomMaxValueAlgo()
}