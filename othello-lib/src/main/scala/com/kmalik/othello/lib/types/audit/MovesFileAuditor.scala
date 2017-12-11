package com.kmalik.othello.lib.types.audit

import java.io.FileWriter

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

import com.kmalik.othello.lib.types.Board
import com.kmalik.othello.lib.types.Board._
import com.kmalik.othello.lib.types.GameAuditor
import com.kmalik.othello.lib.types.Player

class MovesFileAuditor(
    private val filePath: String    
  ) extends GameAuditor {

  var writer:FileWriter = null
  
  def onGameStart(b:Board, p1: Player, p2: Player):Unit = {    
    writer = new FileWriter(filePath, true)
    writer.append("S\n")
  }
  
  def afterGetPossibleMoves(b:Board, p:Player, ms:Seq[Position]):Unit = {    
  }
  
  def afterGetMove(b:Board, p:Player, ms:Seq[Position], m:Position):Unit = {    
  }
  
  def afterMakeMove(b:Board, p:Player, m:Position):Unit = {
    writer.append(p.color.toString).append(",")
          .append(m._1.toString).append(",")
          .append(m._2.toString).append("\n")
  }
    
  def onGameEnd(b:Board, p1: Player, p2: Player):Unit = {
    writer.append("E\n")
    writer.close
  }
  
}

object MovesFileAuditor {
  def apply(filePath:String) = new MovesFileAuditor(filePath)
  
  def read(filePath:String):Seq[Seq[(Color,Position)]] = {
    val games = ArrayBuffer[Seq[(Color,Position)]]()
    
    var game:ArrayBuffer[(Color,Position)] = null
    var dirty = false
    
    val source = Source.fromFile(filePath)
    val iter = source.getLines()
    while (iter.hasNext) {
      val line = iter.next()
      line match {
        case "S" => {
          dirty = true
          game = ArrayBuffer[(Color,Position)]()
        }
        case "E" => {
          games.append(game.toSeq)
          dirty = false
        }
        case _ => {
          if (dirty) {
            val splits = line.split(",").map(Integer.parseInt)
            game.append( (splits(0), (splits(1),splits(2))) )
          } else {
            println(s"Invalid state. Ignore: $line")
          }
        }
      }
    }
    source.close()
    
    games.toSeq
  }
}