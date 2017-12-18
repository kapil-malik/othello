package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._

class FirstMaxFlipsAlgo extends OthelloAlgo {
  
  def select(b:Board, c:Color, ms:Seq[Position]):Position = {    
    ms.map(p => (p, b.getMoveFlipCount(c, p)))
      .maxBy(_._2)
      ._1
  }
}

object FirstMaxFlipsAlgo {
  def apply() = new FirstMaxFlipsAlgo()
}