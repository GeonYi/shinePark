package pjt.geon.shine.core.func.script

import scala.util.control.Breaks._
import pjt.geon.shine._
import pjt.geon.shine.core.func.UnitFunct
import pjt.geon.shine.core.func.condition.Condition
import pjt.geon.shine.command.request.{RunActor, Runner, Util}

/**
 * === function script ===
 *
 * func file include
 * name, desc, param, script
 *
 * */
object Script {

  private lazy val FUNC_SC_KEYWORD_NAME   = "name"
  private lazy val FUNC_SC_KEYWORD_TYPE   = "type"
  private lazy val FUNC_SC_KEYWORD_DESC   = "desc"
  private lazy val FUNC_SC_KEYWORD_PARAM  = "param"
  private lazy val FUNC_SC_KEYWORD_SC     = "script"

  /**
   * load func from script file
   *
   * @param path : absolute path of function file
   * */
  def loadFuncFromFile(path : String) : Unit = {

    var funcName    : String = ""
    var funcType    : String = ""
    var funcDesc    : String = ""
    var funcParam   : String = ""
    var funcScript  : String = ""

    val lines = scala.io.Source.fromFile(path).getLines.toArray

    breakable {
      for(i <- 0 until lines.length){
        val line = lines(i)

        if(line.length == 0 || line == null){
          // do not work
        }
        else if(line.substring(0,4) == FUNC_SC_KEYWORD_NAME){
          funcName = scriptValue(line)
        }
        else if(line.substring(0,4) == FUNC_SC_KEYWORD_TYPE){
          funcType = scriptValue(line)
        }
        else if(line.substring(0,4) == FUNC_SC_KEYWORD_DESC){
          funcDesc = scriptValue(line)
        }
        else if(line.substring(0,5) == FUNC_SC_KEYWORD_PARAM){
          funcParam = scriptValue(line)
        }
        else if(line.substring(0, 6) == FUNC_SC_KEYWORD_SC){
          var scriptIdx = i
          do{
            scriptIdx = scriptIdx + 1
            val tmpStr = lines(scriptIdx).replace("[", "").replace("]", " ").replace("\t", "").replace(" ","")
            if(tmpStr.length != 0 || tmpStr != ""){
              funcScript = funcScript + lines(scriptIdx).replace("[", "").replace("]", " ").replace("\t", "") +
                SCALA_NEW_LINE
            }
          }while( lines(scriptIdx) != "]" )
          break //for segment end point
        }
      }
    }

    funcScript = funcScript.substring(0, funcScript.length -1)

    val parmas : Array[(String, String, String, Any)] = {
      funcParam.split(",").map(f => {
        val paramValName = f.substring(0, f.indexOf("{"))
        val paramType = paramValue(f)
        val paramDesc = f.substring(f.indexOf(":") + 1)
        // null mean that default value
        (paramValName, paramDesc, paramType, null)
      })
    }

    if(funcType == FUNC_TYPE_SINGLE){
      UnitFunct.functList +=
        funcName -> UnitFunct.createFunctWithMethodWithoutPreScript(funcName, FUNC_BY_SCRIPT,
          funcType, funcDesc, funcScript, UnitFunct.createFunctParam(parmas), null)
    }
    else if (funcType == FUNC_TYPE_MULTI){
      val splitedScript = splitScript(funcScript)
      UnitFunct.functList +=
        funcName -> UnitFunct.createFunctWithMethodAndPreScript(funcName, FUNC_BY_SCRIPT,
          funcType, funcDesc, splitedScript._1, splitedScript._2, UnitFunct.createFunctParam(parmas), null)
    }
    else{
      println("function script load fail.  check function type")
    }
  }

  private def scriptValue(str : String) : String = {
    str.substring(str.indexOf("[")+1, str.indexOf("]"))
  }

  private def paramValue(str : String) : String = {
    str.substring(str.indexOf("{")+1, str.indexOf("}"))
  }

  private def scriptFuncValue(str : String) : String = {
    val tmpStr = str.replace(" ","")
    tmpStr.substring(tmpStr.indexOf(":")+1, tmpStr.length)
  }

  /**
   * run script
   * */
  def runScriptFunct(by: String, scriptName : String, params : String*) : Any = {

    val settingParamsScript = setParam(
      UnitFunct.functList.apply(scriptName)._1.funcScript,
      UnitFunct.functList.apply(scriptName)._1.funcParams.map{f => (f.name)}.toArray,
      params.toArray)

    runScriptFunct(by, settingParamsScript)
  }

  def runScriptFunct(by:String, scriptStr : String) : Any = {
    var script = scriptStr.trim
    val FUNC_SC_REG : String = "[a-zA-Z]{1,}[(][-/+a-zA-Z,.0-9]{1,}[)]"

    val TMP_FUNC_EXPRESS : String = "####FuNcTiOn####"
    var innerFunctCount = script.count(_ == ')') - 1

    //inner function run => {plus(1,plus(2,3)) => plus(1,5)}
    //func allow duplicate 100
    breakable {
      for(i <- 0 to 100){
        if(innerFunctCount == 0){
          break
        }

        val unitFunc =  FUNC_SC_REG.r.findFirstIn(script).map(f => { f }).get
        script = script.replaceFirst(FUNC_SC_REG, TMP_FUNC_EXPRESS)

        val uniFuncResult = RunActor.runSingleScriptFuncWithinBasicFuncWithParams(
            by,
            unitFunc.substring(0, unitFunc.indexOf("(")),
            unitFunc.substring(unitFunc.indexOf("(")+1, unitFunc.length-1).split(","):_*
        )
        script = script.replace(TMP_FUNC_EXPRESS, uniFuncResult.toString)
        innerFunctCount -= 1
      }
    }

    //outer function run2
    RunActor.runSingleScriptFuncWithinBasicFuncWithParams(
      by,
      script.substring(0, script.indexOf("(")),
      script.substring(script.indexOf("(")+1, script.indexOf(")")).split(","):_*
    )
  }

  /**
   * set parameters
   *
   * */
  private def setParam(script : String, scriptParams : Array[String], inParams : Array[String]) : String = {
    var result = script.replace(" ", "")
    for(idx <- scriptParams.indices){
      val reg = "[$]{1}" + scriptParams.apply(idx)
      result = reg.r.replaceAllIn(result, inParams(idx))
    }
    result
  }

  def runMultiScript(script : String) : Any = {
    lazy val by = "system"
    lazy val conditionSaperator = "@@"
    val funcType    = Util.funcType(script)
    val funcScript  = UnitFunct.functList(funcType._1)._1.script.replace("#", "double "
      +FUNC_RESERVED_VALUE_DOUBLE + " ")
    val paramSettingScript = setParam(funcScript, UnitFunct.functList.apply(funcType._1)
      ._1.funcParams.map{_.name}.toArray, funcType._2)
    val strArr = paramSettingScript.split("\n")

    var resultValue : Any = null

    var strIdx = 0
    do{
      var ifTmpSyntax = ""
      val inStr : String = strArr(strIdx).trim
      if(inStr.length > 0){
        // run ones
        if(inStr.startsWith(SYS_SYNTAX_IF)){
          do{
            val conditionStr = strArr(strIdx).replace(" ", "")
            ifTmpSyntax = ifTmpSyntax.concat(if(conditionStr.startsWith("=>")){conditionStr + conditionSaperator}else{strArr(strIdx).trim})
            strIdx += 1
          }while(strArr(strIdx).trim.toLowerCase != "endif")

          val unitConditionTuple2 = ifTmpSyntax.split(conditionSaperator).map(str => {
            val conditionWithFunc = str.split("=>")
            (conditionWithFunc(0), if(conditionWithFunc.length == 1){null}else{conditionWithFunc(1)})
          }) ++ Array(("endif", null))


          //todo : 안에서 command 돌리고 결과 주는걸로 바꾸자....

          Runner.run(Condition.checkConditionSyntax(unitConditionTuple2, by).toString, by)
          resultValue = RunActor.valueFromValueContainer(FUNC_RESERVED_VALUE_DOUBLE, "system")
        }
        else{
          Runner.run(strArr(strIdx), by)
        }
      }
      strIdx += 1
    }while(strIdx < strArr.length)

    if(resultValue != null){
      resultValue
    }else{
      null
    }
  }

  /**
   * remove dollar($) mark
   * */
  @deprecated
  private def remove$(inStr : String) : String = {
    val str : String = inStr.replace(" ", "")
    if(str.length >= KEYWORD_VALUE_TYPE_INT.length + 1 && str.substring(0,3) == KEYWORD_VALUE_TYPE_INT){
      KEYWORD_VALUE_TYPE_INT + " " +  // "int "
        "_" + str.substring(KEYWORD_VALUE_TYPE_INT.length, str.indexOf("=")) + " = " +  // "int _originalName = "
        str.substring(str.indexOf("=") + 1, str.length)
    } else if(str.length >= KEYWORD_VALUE_TYPE_DOUBLE.length + 1 && str.substring(0,6) == KEYWORD_VALUE_TYPE_DOUBLE ){
      KEYWORD_VALUE_TYPE_DOUBLE + " " +
        "_" + str.substring(KEYWORD_VALUE_TYPE_DOUBLE.length, str.indexOf("=")) + " = " +
        str.substring(str.indexOf("=") + 1, str.length)
    } else if(str.length >= KEYWORD_VALUE_TYPE_ROW.length+1 && str.substring(0,3) == KEYWORD_VALUE_TYPE_ROW ){
      KEYWORD_VALUE_TYPE_ROW + " " +
        "_" + str.substring(KEYWORD_VALUE_TYPE_ROW.length, str.indexOf("=")) + " = " +
        str.substring(str.indexOf("=") + 1, str.length)
    } else if(str.length >= KEYWORD_VALUE_TYPE_DATAFRAME.length + 1 && str.substring(0,9) == KEYWORD_VALUE_TYPE_DATAFRAME ){
      KEYWORD_VALUE_TYPE_DATAFRAME + " " +
        "_" + str.substring(KEYWORD_VALUE_TYPE_DATAFRAME.length, str.indexOf("=")) + " = " +
        str.substring(str.indexOf("=") + 1, str.length)
    } else {
      inStr
    }
  }

  /**
   * ===split "outer script" and "inner script"
   *
    *
    * @param script full script include outer script and inner script
   * @return (outerScript, innerScript)
   * */
  def splitScript(script : String) : (String, String) = {
    val scriptArr = script.split("\n")
    val scriptLen = scriptArr.length

    val firstInnerPoint : Int = scriptArr.indexWhere(p => {p.trim.startsWith("inner")})
    val lastInnerPoint  : Int = scriptArr.indexWhere(p => {p.trim.equals("}")}, firstInnerPoint + 1)

    val firstOuterPoint : Int = scriptArr.indexWhere(p => {p.trim.startsWith("outer")})
    val lastOuterPoint  : Int = scriptArr.indexWhere(p => {p.trim.equals("}")}, firstOuterPoint+1)

    ( scriptArr.drop(firstOuterPoint + 1).dropRight(scriptLen - lastOuterPoint).mkString("\n"),
      scriptArr.drop(firstInnerPoint + 1).dropRight(scriptLen - lastInnerPoint).mkString("\n"))
  }

  /**
   * === running to outer script
   *
    *
    * @return script success
   * */
  def runOuterScript(script : String) : Unit = {
    if(UnitFunct.functList.apply(Util.getFuncName(script))._1.funcType == FUNC_TYPE_MULTI){
//      val makeResultDoubleValue : String = "double " + FUNC_RESERVED_VALUE_DOUBLE + " = 0.0"
//      val makeResultIntValue    : String = "int "    + FUNC_RESERVED_VALUE_INT    + " = 0"

      val preFuncScript = UnitFunct.functList.apply(Util.getFuncName(script))._1.funcPreScript

      println("[System Print] : --------------------------------------")
      println("[System Print] : running pre-function script")
      println("[System Print] : --------------------------------------")

      preFuncScript.split("\n").foreach(f=>{
        Runner.run(f.trim, "system")
      })

      //add for result value.  2015.09.07
//      Runner.runCommand(makeResultDoubleValue , "system")
//      Runner.runCommand(makeResultIntValue    , "system")

      println("[System Print] : end script")
      println("[System Print] : --------------------------------------")
    }
  }
}

