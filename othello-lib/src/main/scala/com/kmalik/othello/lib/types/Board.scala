package com.kmalik.othello.lib.types

import com.kmalik.othello.lib.types.Board._

class Board(private val state:Array[Int]) {

  def move(c:Color, p:Position):Unit = {
    if (!isMoveValid(c, p)) {
      throw new RuntimeException("Invalid move")
    }
    
    state(serPos(p)) = c
    
    DIRECTIONS.foreach(d => {
      val (i,j) = d
      val flipCount = getDirectionFlipCount(c, p, i, j)
      if (flipCount > 0) {
        for (k<- 1 to flipCount) {
          state(serPos((p._1 + i*k, p._2 + j*k))) = c
        }
      }
    })
    
  }
  
  def isMoveValid(c:Color, p:Position):Boolean = {
    // Check valid board position
    if (!assertColor(c) || !assertPosition(p)) {
      return false;
    }
    
    // Check empty position
    if (getPositionValue(p) != EMPTY) {
      return false;
    }
    
    return DIRECTIONS.exists(d => getDirectionFlipCount(c, p, d._1, d._2) > 0)
  }
    
  def getMoveFlipCount(c:Color, p:Position):Int = {
    if (!isMoveValid(c, p)) return 0;
    
    DIRECTIONS.map(d => getDirectionFlipCount(c, p, d._1, d._2))
              .reduce(_+_)
  }
  
  def getPossibleMoves(c:Color):Seq[Position] = {
    (0 to SIZE - 1).map(deserPos).filter(p => isMoveValid(c, p))
  }
  
  /**
   * Counts for (WHITE, BLACK, EMPTY)
   */
  def getCounts():(Int, Int, Int) = {
    (state.filter(_ == WHITE).size,
     state.filter(_ == BLACK).size,
     state.filter(_ == EMPTY).size)
  }
  
  def prettyPrint(
    white:String = WHITE.toString(),
    black:String = BLACK.toString(),
    empty:String = EMPTY.toString()):String = {
    
    val str = serialize()

    (0 to LEN - 1).map(j => str.substring(LEN*j, LEN*j + LEN))
                  .map(_.replaceAll(WHITE.toString, white)
                        .replaceAll(BLACK.toString, black)
                        .replaceAll(EMPTY.toString, empty))
                  .mkString("\n")
  }
  
  private def getPositionValue(p:Position):Color = state(serPos(p))
      
  private def getDirectionFlipCount(c:Color, p:Position, dx:Int, dy:Int):Int = {
    var _p = (p._1 + dx, p._2 + dy)
    val c2 = other(c)
    
    var range = 0
    while (assertPosition(_p) && getPositionValue(_p) == c2) {
      range = range + 1;
      _p = (_p._1 + dx, _p._2 + dy)
    }
    if (range == 0) {
      return 0
    }
    if (assertPosition(_p) && (getPositionValue(_p) == c)) {
      return range
    }
    
    return 0
  }

  
  def serialize():String = state.mkString
  
}

object Board {

  type Position = (Int, Int)
  type Color = Int
  
  val WHITE = 1
  val BLACK = 2
  val EMPTY = 0
  
  private val LEN = 8
  private val SIZE = LEN*LEN
  private val DIRECTIONS = 
    Seq((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))      
  
  def apply(_state:Array[Int]):Board = new Board(_state)
  
  def apply(_state:Array[Array[Int]]):Board = {
    val _state2 = new Array[Int](SIZE)
    _state.zipWithIndex.foreach(p1 => {
      val a = p1._1
      val i1 = p1._2
      a.zipWithIndex.foreach(p2 => {
        val v = p2._1
        val i2 = p2._2
        _state2(LEN*i1 + i2) = v
      })
    })
    new Board(_state2)
  }
  
  def apply(_state:String):Board = {
    val _state2 = _state.map(c => Integer.parseInt(c.toString)).toArray
    new Board(_state2)
  }
  
  def newBoard():Board = {
    val a = new Array[Int](SIZE);
    val mid = (LEN/2) -1
    a(serPos((mid, mid))) = BLACK;
    a(serPos((mid, mid + 1))) = WHITE;
    a(serPos((mid + 1, mid))) = WHITE;
    a(serPos((mid + 1, mid + 1))) = BLACK;
    Board(a);
  }
  
  private def other(c:Color):Color = if (c==WHITE) BLACK else WHITE;
  
  private def assertColor(c:Color):Boolean = c==WHITE || c==BLACK
  
  private def assertPosition(p:Position):Boolean = 
    (p._1 >= 0 && p._1 < LEN && p._2 >= 0 && p._2 < LEN)
  
  private def serPos(p:Position):Int = (p._1 * LEN) + p._2

  private def deserPos(i:Int):Position = (i / LEN, i % LEN)
  
}