package pjt.geon.shine.command.request

import pjt.geon.shine._

object Runner {

  /**
    * === run command proc ===
    *
    *  this method split value by key word.
    *  finally call [[Proxy]] object
    *
    * @param commandWithArray command with args
    * @param by user type, user or system
    * @author yigeon
    * @since 2015.08.25
    * */
  def run(commandWithArray : String, by : String) : Unit = {
    //add for system run
    if(by != "system" && by != "user"){
      Util.sysPrintln("bad user, check user type. may be can't understand, than call me.  82-10-414*-949*" + "\n" + "no happen")
    }
    else if(commandWithArray.startsWith("//") || commandWithArray.replace(" ","").length == 0){
      //do nothing
    }
    else{
      val convertedValue = this.convert(commandWithArray, by)

      try{
        convertedValue._1.toLowerCase match {
          // create value command
          case COMMAND_VALUE => {this.value(convertedValue._2, by)}
          // call func command
          case COMMAND_FUNC  => {this.func(convertedValue._2, by)}
        }
      } catch {
        // wrong command
        case _ : Throwable => Util.sysPrintln("check command plz -_-")
      }
    }
  }

  /**
    * === command convert ===
    *
    * convert to tuple2(command type, command params array)
    * {{{
    *  // ==============================================================
    *  //  check value command
    *  // ==============================================================
    *  //  condition
    *  //   1. no have Parenthesis (= get value)
    *  //     ex_1)intValue
    *  //     ex_2)dfValue
    *  //   2. has equal symbol(=)
    *  //     ex_1)int intValue = 10
    *  //     ex_2)int intValue = plus(10,20)
    *  //     ex_3)dataframe dfValue = load(file,path)
    *  // ==============================================================
    *  }}}
    *
    * @param inputString stand input string
    * @param by user type
    * @return [[Tuple2]] : command type, command parameters
    *
    * */
  def convert(inputString : String, by : String) : (String, Array[String]) = {

    var commandType   : String        = null
    var commandParams : Array[String] = null

    // remove all space
    val removeSpaceInputStr : String = inputString.replace(" ", "")

    // get value keyword
    // example : $tempValue
    if(Util.isGetValueNameByUser(inputString)){
      return (COMMAND_VALUE, Array(inputString))
    }
    else if(Util.isAssignValue(inputString)){
      if(!removeSpaceInputStr.substring(3, removeSpaceInputStr.indexOf("=")).matches(REG_CREATE_VALUE_TYPE)){
        // do nothing
        // create value syntax error
      }else{
        if(removeSpaceInputStr.length >= 3 && removeSpaceInputStr.substring(0,3) == KEYWORD_VALUE_TYPE_INT ){
          commandType = COMMAND_VALUE
          commandParams = Array(
            KEYWORD_VALUE_TYPE_INT,
            removeSpaceInputStr.substring(3, removeSpaceInputStr.indexOf("=")),
            removeSpaceInputStr.substring(removeSpaceInputStr.indexOf("=")+1, removeSpaceInputStr.length)
          )
        }
        else if(removeSpaceInputStr.length >= 3 && removeSpaceInputStr.substring(0,3) == KEYWORD_VALUE_TYPE_ROW ){
          commandType = COMMAND_VALUE
          commandParams = Array(
            KEYWORD_VALUE_TYPE_ROW,
            removeSpaceInputStr.substring(3, removeSpaceInputStr.indexOf("=")),
            removeSpaceInputStr.substring(removeSpaceInputStr.indexOf("=")+1, removeSpaceInputStr.length)
          )
        }
        else if(removeSpaceInputStr.length >= 6 && removeSpaceInputStr.substring(0,6) == KEYWORD_VALUE_TYPE_DOUBLE ){
          commandType = COMMAND_VALUE
          commandParams = Array(
            KEYWORD_VALUE_TYPE_DOUBLE,
            removeSpaceInputStr.substring(6, removeSpaceInputStr.indexOf("=")), //변수명
            removeSpaceInputStr.substring(removeSpaceInputStr.indexOf("=")+1, removeSpaceInputStr.length) // 값
          )
        }
        else if(removeSpaceInputStr.length >= 9 && removeSpaceInputStr.substring(0,9) == KEYWORD_VALUE_TYPE_DATAFRAME ){
          commandType = COMMAND_VALUE
          commandParams = Array(
            KEYWORD_VALUE_TYPE_DATAFRAME,
            removeSpaceInputStr.substring(9, removeSpaceInputStr.indexOf("=")), //변수명
            removeSpaceInputStr.substring(removeSpaceInputStr.indexOf("=")+1, removeSpaceInputStr.length) // 값
          )
        }
      }
    }
    else if(Util.isFunct(inputString)){
      commandType = COMMAND_FUNC
      commandParams = Array(inputString)
    }

    (commandType, commandParams)
  }

  /**
    * === make value, view value ===
    * only check number of args => be change
    *
    * @author yigeon
    * @param commandArgs Array(type, name, init value)
    * @param by system or user
    * @since 2015.06.29
    * @note change system standard out.
    * */
  private def value(commandArgs : Array[String], by : String) = {
    // when make value
    if(commandArgs.length == 3){
      //type name value
      try{
        Util.isFunct(commandArgs(2))
        Proxy.setValue(commandArgs(0), commandArgs(1), Util.isFunct(commandArgs(2)), commandArgs(2), by)
      } catch {
        case e : Exception => println("---------------------22"); Util.sysPrintln("check value command.  command : [" + commandArgs.mkString(",") + "]")
      }
    }
    // when get(view, print) value
    else if (commandArgs.length == 1){
      Proxy.getValue(commandArgs(0), by)
    }
    else{
      Util.sysPrintln("check value command.  command : [" + commandArgs.mkString(",") + "]")
    }
  }

  /**
    * === call func, see func info ===
    *
    * {{{  call func : f +(a,b)
    * info func : f info(+)}}}
    *
    * @param args argument for running function
    * @param by user type
    * @author yigeon
    * @since 2015.60.29
    * */
  private def func(args : Array[String], by : String) = {
    Proxy.runFunc(args(0), by)
  }

  //  private def value(value : String) : String = {
  //    if(value(0) == '$'){
  //      value.substring(1,value.length)
  //    }else{
  //      value
  //    }
  //  }
}
