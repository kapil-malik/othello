package com.kmalik.othello.lib

object Utils {
  
  def readArg(args:Array[String],name:String):String = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length())
    } else {
      throw new RuntimeException(s"$name not provided")
    }
  }
  
  def readArg(args:Array[String],name:String,defValue:String):String = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length())
    } else {
      println(s"No argument passed for $name, using $defValue") 
      defValue
    }
  }
  
  def readArg(args:Array[String],name:String,defValue:Int):Int = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length()).toInt
    } else {
      println(s"No argument passed for $name, using $defValue") 
      defValue
    }
  }
  
  def readArg(args:Array[String],name:String,defValue:Long):Long = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length()).toLong
    } else {
      println(s"No argument passed for $name, using $defValue") 
      defValue
    }
  }
  
  def readArg(args:Array[String],name:String,defValue:Boolean):Boolean = {
    val prefix = "--"+name+"="
    val argOpt = args.find(_.startsWith(prefix))
    if (argOpt.isDefined) {
      argOpt.get.substring(prefix.length()).toBoolean
    } else {
      println(s"No argument passed for $name, using $defValue") 
      defValue
    }
  }  
}