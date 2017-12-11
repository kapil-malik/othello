package com.kmalik.othello.lib

import com.kmalik.othello.lib.algo.RandomMaxValueAlgo
import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._
import com.kmalik.othello.lib.types.Game
import com.kmalik.othello.lib.types.Player
import com.kmalik.othello.lib.types.audit.MovesFileAuditor
import com.kmalik.othello.lib.algo.OthelloAlgo
import com.kmalik.othello.lib.algo.FirstMoveAlgo
import com.kmalik.othello.lib.algo.RandomMoveAlgo
import com.kmalik.othello.lib.algo.FirstMaxValueAlgo
import com.kmalik.othello.lib.types.audit.SimpleAuditor


object Main {
  
  def main(args:Array[String]):Unit = {
    
    val mode = readArg(args, "mode", "play")
    
    mode match {
      case "genMoveData" => genMoveData(args)
      case "readMoveData" => readMoveData(args)
      case "play" => play(args)
      case _ => help(args)
    }
    
  }

  private def help(args:Array[String]):Unit = {
    println("Usage: --mode=[play | genMoveData | readMoveData | help]")
    
    val api = readArg(args, "api", "help")
    
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
    val pace = readArg(args, "pace", 1)
    val algo1 = getAlgo(readArg(args, "algo1", "randomMax"))
    val algo2 = getAlgo(readArg(args, "algo2", "randomMax"))
    val name1 = readArg(args, "name1", "Black")
    val name2 = readArg(args, "name2", "White")
    Game.newGame(
        new Player(name1, BLACK, algo1), 
        new Player(name2, WHITE, algo2), 
        SimpleAuditor(pace))
      .play()
  }
  
  private def readMoveData(args:Array[String]):Unit = {
    val file = readArg(args, "file")
    val gameCount = readArg(args, "gameCount", 1)
    val pace = readArg(args, "pace", 1)
    
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
    val file = readArg(args, "file")
    val gameCount = readArg(args, "gameCount", 1)
    val algo1 = getAlgo(readArg(args, "algo1", "randomMax"))
    val algo2 = getAlgo(readArg(args, "algo2", "randomMax"))
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
      case "firstmax" => FirstMaxValueAlgo()
      case "randommax" => RandomMaxValueAlgo()
      case _ => RandomMoveAlgo()
    }
  }
  
  private def readArg(args:Array[String],name:String):String = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length())
    } else {
      throw new RuntimeException(s"$name not provided")
    }
  }
  
  private def readArg(args:Array[String],name:String,defValue:String):String = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) argOpt.get.substring(prefix.length()) else defValue
  }
  
  private def readArg(args:Array[String],name:String,defValue:Int):Int = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) argOpt.get.substring(prefix.length()).toInt else defValue
  }
  
  private def readArg(args:Array[String],name:String,defValue:Long):Long = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) argOpt.get.substring(prefix.length()).toLong else defValue
  }
  
  private def readArg(args:Array[String],name:String,defValue:Boolean):Boolean = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) argOpt.get.substring(prefix.length()).toBoolean else defValue
  }
  
}