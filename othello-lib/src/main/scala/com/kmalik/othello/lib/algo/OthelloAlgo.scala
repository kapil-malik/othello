package com.kmalik.othello.lib.algo

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._

trait OthelloAlgo {
 
  def select(b:Board, c:Color, ms:Seq[Position]):Position
  
}