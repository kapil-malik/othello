package com.kmalik.othello.lib

import com.kmalik.othello.lib.algo.RandomMaxFlipsAlgo
import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._
import com.kmalik.othello.lib.types.Game
import com.kmalik.othello.lib.types.Player
import com.kmalik.othello.lib.types.audit.MovesFileAuditor
import com.kmalik.othello.lib.algo.OthelloAlgo
import com.kmalik.othello.lib.algo.FirstMoveAlgo
import com.kmalik.othello.lib.algo.RandomMoveAlgo
import com.kmalik.othello.lib.algo.FirstMaxFlipsAlgo
import com.kmalik.othello.lib.types.audit.SimpleAuditor


object Main {
  
  def main(args:Array[String]):Unit = {
    
    val mode = Utils.readArg(args, "mode", "play")
    
    mode match {
      case "genMoveData" => genMoveData(args)
      case "readMoveData" => readMoveData(args)
      case "play" => play(args)
      case _ => help(args)
    }
    
  }

  private def help(args:Array[String]):Unit = {
    println("Usage: --mode=[play | genMoveData | readMoveData | help]")
    
    val api = Utils.readArg(args, "api", "help")
    
    api match {
      case "help" => {
        print("help usage: --mode=help")
        print(" --api=[play | genMoveData | readMoveData | help]")
        println()
      }
      case "play" => {
        print("play usage: --mode=play")
        print(" --algo1=[firstMove | randomMove | firstMax | randomMax]")
        print(" --algo2=[firstMove | randomMove | firstMax | randomMax]")
        print(" --name1=[Black]")
        print(" --name2=[White]")
        print(" --pace=[1]")
        println()
      }
      case "genMoveData" => {
        print("genMoveData usage: --mode=genMoveData")
        print(" --algo1=[firstMove | randomMove | firstMax | randomMax]")
        print(" --algo2=[firstMove | randomMove | firstMax | randomMax]")
        print(" --file={file:///}")
        print(" --gameCount=[1]")
        println()
      }
      case "readMoveData" => {
        print("readMoveData usage: --mode=readMoveData")
        print(" --file={file:///}")
        print(" --gameCount=[1]")
        print(" --pace=[1]")
        println()
      }
                
      case _ => {}
    }
    
  }
  
  private def play(args:Array[String]):Unit = {
    val pace = Utils.readArg(args, "pace", 1)
    val algo1 = getAlgo(Utils.readArg(args, "algo1", "randomMax"))
    val algo2 = getAlgo(Utils.readArg(args, "algo2", "randomMax"))
    val name1 = Utils.readArg(args, "name1", "Black")
    val name2 = Utils.readArg(args, "name2", "White")
    Game.newGame(
        new Player(name1, BLACK, algo1), 
        new Player(name2, WHITE, algo2), 
        SimpleAuditor(pace))
      .play()
  }
  
  private def readMoveData(args:Array[String]):Unit = {
    val file = Utils.readArg(args, "file")
    val gameCount = Utils.readArg(args, "gameCount", 1)
    val pace = Utils.readArg(args, "pace", 1)
    
    val gameHistorySeq = MovesFileAuditor.read(file)
    for (i<- 0 to gameCount) {
      val gameHistory = gameHistorySeq(i)
      val board = Board.newBoard()
      println(board.prettyPrint("W", "B", "_"))
      Thread.sleep(pace*500)
      gameHistory.foreach(move => {
        println(s"Move by ${move._1}: ${move._2}")
        board.move(move._1, move._2)
        println(board.prettyPrint("W", "B", "_"))      
        Thread.sleep(pace*1000)
      })
      println(s"Scores: ${board.getCounts}")
      Thread.sleep(pace*1000)
    }
  }
  
  private def genMoveData(args:Array[String]):Unit = {
    val file = Utils.readArg(args, "file")
    val gameCount = Utils.readArg(args, "gameCount", 1)
    val algo1 = getAlgo(Utils.readArg(args, "algo1", "randomMax"))
    val algo2 = getAlgo(Utils.readArg(args, "algo2", "randomMax"))
    val cp = if (gameCount > 10) gameCount / 10 else gameCount
    
    println("Generating data")
    (1 to gameCount).foreach(i => {
      Game.newGame(
        new Player("Black", BLACK, algo1), 
        new Player("White", WHITE, algo2), 
        MovesFileAuditor(file))
      .play()
      if (i % cp == 0) {
        println(i)
      }
    })
    println("Generation complete")
  }
  
  private def getAlgo(algo:String):OthelloAlgo = {
    algo.toLowerCase() match {
      case "firstmove" => FirstMoveAlgo()
      case "randommove" => RandomMoveAlgo()
      case "firstmax" => FirstMaxFlipsAlgo()
      case "randommax" => RandomMaxFlipsAlgo()
      case _ => {
        println(s"No algo found for $algo, using RandomMove")
        RandomMoveAlgo()
      }
    }
  }
  
}