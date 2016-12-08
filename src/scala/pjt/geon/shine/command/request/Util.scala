package pjt.geon.shine.command.request

import pjt.geon.shine._
import pjt.geon.shine.core.value.ValueContainer
import pjt.geon.shine.core.value.base.IntegerType

/**
 * === common util ===
 *
 * */
object Util {
  /**
   * common system print
   * */
  def sysPrintln(msg : String) : Unit = {
    println (COMMAND_SYSTEM_OUTPUT + msg)
  }

  /**
   * check command string match "&#94;[a-zA-Z]{2,}[(].*[)]"
   * */
  def isFunct(str : String) = {
    str.matches(REG_FUNCTION_TYPE)
  }

  /**
   * has equal sing count > 0
   * */
  def isAssignValue(commandStr : String) : Boolean = {
    commandStr.count(_ == '=') > 0
  }

  /**
   * check command string match "[a-zA-Z]{1}[a-zA-Z0-9]{0,49}"
   * */
  def isGetValueNameByUser(commandStr : String) : Boolean = {
    commandStr.matches(REG_USER_VALUE_TYPE)
  }

  /**
    * get function name by script
    *
    * @param script function script
    * @example when "plus(1,2)" then "plus"
    * @example when "minus(plus(1,2),1)" => "minus"
    * */
  def getFuncName(script : String) : String = {
    script.substring(0,script.indexOf("("))
  }

  /**
    * create func tuple(funcName, Params*)
    *
    * @param strScript function script
    * @example plus(1,2) => (plus, Array(1,2):_*)
    * */
  def funcType(strScript : String) : (String, Array[String]) = {
    val firPoint = strScript.indexOf("(") + 1
    val secPoint = strScript.indexOf(")")

    var returnVal =
      (strScript.substring(0, firPoint - 1 ),   //function name
        strScript.substring(firPoint, secPoint) //function params
          .replace(" ", "")
          .split(FUNC_PARAM_SEP))

    if(returnVal._1 == KEYWORD_FUNC_DATAFRAME && returnVal._2(0) != KEYWORD_FUNC_DATAFRAME_PARAM_PRINT){
      returnVal = dfMapType(strScript)
    }

    returnVal
  }

  private def dfMapType(str : String) : (String, Array[String]) = {
    val firstPoint  = str.indexOf(',')
    val secondPoint = str.indexOf(',', firstPoint   + 1)
    val thirdPoint  = str.indexOf(',', secondPoint  + 1)

    val dfFuncType = str.substring(str.indexOf("(") + 1, str.indexOf(','))

    //if replace
    if(dfFuncType == KEYWORD_FUNC_DATAFRAME_PARAM_MAP){
      ( KEYWORD_FUNC_DATAFRAME,
        Array(
          KEYWORD_FUNC_DATAFRAME_PARAM_MAP,
          str.substring(firstPoint  + 1, secondPoint  ),
          str.substring(secondPoint + 1, thirdPoint   ),
          str.substring(thirdPoint  + 1, str.length -1))
        )
    }
    else if(dfFuncType == KEYWORD_FUNC_DATAFRAME_PARAM_REDUCE) {
      ( KEYWORD_FUNC_DATAFRAME,
        Array(
          KEYWORD_FUNC_DATAFRAME_PARAM_REDUCE,
          str.substring(firstPoint  + 1, secondPoint   ),
          str.substring(secondPoint + 1, str.length -1 ))
        )
    }else{
      null
    }
  }

  def isSingleDeepsFunc(script : String) : Boolean = {
    var aa = script.count(p => {p == ')' || p == '('})

    if(script.count(p => {p == ')' || p == '('}) == 2){ true } else { false }
  }

  /**
    * check numeric type
    * */
  def checkNumericValue(in : Any) : Boolean = {
    //    in.getClass.getName match {
    //      case java.lang.Integer. => true
    //      case java.lang.Double  => true
    //      case _  => false
    //    }
    true
  }

  def getDouble(value : Any) : java.lang.Double = {
    // if value in "value container"
    if(value.getClass == IntegerType){
      new java.lang.Double(ValueContainer.getValue(SYS_VALUE_CONTAINER_USER, value.toString).toString)
    } else {
      new java.lang.Double(value.toString)
    }
  }
}
