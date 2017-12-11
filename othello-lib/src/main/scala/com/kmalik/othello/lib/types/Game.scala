package com.kmalik.othello.lib.types

import com.kmalik.othello.lib.algo.OthelloAlgo
import com.kmalik.othello.lib.types.Board.BLACK
import com.kmalik.othello.lib.types.Board.WHITE
import com.kmalik.othello.lib.types.audit.SimpleAuditor

class Game(
  private val board: Board,
  private val player1: Player,
  private val player2: Player,
  private val audit:GameAuditor
) {
  
  def play():Unit = {
    audit.onGameStart(board, player1, player2);
    
    var over = false
    var curr = player1
    var other = player2
    
    while (!over) {
      val moves = board.getPossibleMoves(curr.color)
      audit.afterGetPossibleMoves(board, curr, moves)
      
      if (moves.size > 0) {
        val move = curr.getMove(board, moves)
        audit.afterGetMove(board, curr, moves, move)
        
        board.move(curr.color, move)
        audit.afterMakeMove(board, curr, move)
        
      } else {
        if (board.getPossibleMoves(other.color).size == 0) {
          over = true
        }
      }
      
      val temp = other
      other = curr
      curr = temp
    }
    
    audit.onGameEnd(board, player1, player2)
  }
}

object Game {
  
  def newGame(player1:Player, player2:Player, auditor:GameAuditor):Game = {
    new Game(Board.newBoard(),
             player1,
             player2,
             auditor
            )
  }
  
  def simpleGame(blackAlgo:OthelloAlgo, whiteAlgo:OthelloAlgo, pace:Int = 1):Game = {
    new Game(Board.newBoard(),
             new Player("Black", BLACK, blackAlgo),
             new Player("White", WHITE, whiteAlgo),
             SimpleAuditor(pace)
            )
  }
  
}