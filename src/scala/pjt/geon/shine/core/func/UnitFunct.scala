package pjt.geon.shine.core.func

import java.lang.reflect.Method

import pjt.geon.shine._
import pjt.geon.shine.command.request.Util
import pjt.geon.shine.core.func.basic.number.NumberBasicFunc


/**
 * === Function ===
 * Heart's func class
 */
class UnitFunct (val funcName   : String,
                 val funcBy     : String,
                 val funcType   : String = FUNC_TYPE_SINGLE,
                 val funcDesc   : String = "",
                 val funcPreScript : String = "",
                 val funcScript : String = "",
                 val funcParams : Traversable[FunctParam] = null ){

  val name    : String = funcName
  val by      : String = funcBy
  val type_   : String = funcType
  val desc    : String = funcDesc
  val preScript    : String = funcPreScript
  val script  : String = funcScript
  var params  : Traversable[FunctParam] = funcParams

  // only exist function name
  def this (funcName : String, funcBy : String) = {
    this(funcName, funcBy, FUNC_TYPE_SINGLE,  "no have description", "", "", null)
  }

  // exist function name and description
  def this (funcName : String, funcBy : String, funcDesc : String) = {
    this(funcName, funcBy, FUNC_TYPE_SINGLE,  funcDesc, "", "", null)
  }

  def setParams(params : Traversable[FunctParam]) = {
    this.params = params
  }

  def println : Unit = {
    Console.println("==============================================================================")
    Console.println("FUNCTION INFORMATION")
    Console.println("==============================================================================")
    Console.println("name      : [" + this.name    + "]")
    Console.println("by        : [" + this.by      + "]")
    Console.println("type      : [" + this.type_   + "]")
    Console.println("desc      : [" + this.desc    +  "]")
    Console.println("preScript : [" + "\n" + this.preScript +  "\n" + "]")
    Console.println("script    : [" + "\n" + this.script    +  "\n" + "]")
    Console.println("==============================================================================")
    Console.println("parameter(s) info")
    Console.println("------------------------------------------------------------------------------")
    this.params.foreach(f => {
      Console.println("  param name          : [" + f.name + "]")
      Console.println("  param desc          : [" + f.desc + "]")
      Console.println("  param allow type    : [" + f.allowType + "]")
      Console.println("  param default value : [" + f.defaultVal + "]")
      Console.println("------------------------------------------------------------------------------")
    })
    Console.println("==============================================================================")
  }
}

object UnitFunct {

  /** ['func_name'([[UnitFunct]]class, [[Method]]class)]*/
  var functList : Map[String, (UnitFunct, Method)] = basicFuncLoad

  private def basicFuncLoad : Map[String, (UnitFunct, Method)] = {
    Map(
      "plus" ->
        this.createFunctWithMethodWithoutPreScript("plus", FUNC_BY_SYSTEM, FUNC_TYPE_SINGLE, "{first value} + {second value}", null,
        this.createFunctParamWithNoName(Array( ("first number type value",  "number", 0), ("second number type value", "number", 0))),
        NumberBasicFunc.getClass),
      "minus" ->
        this.createFunctWithMethodWithoutPreScript("minus", FUNC_BY_SYSTEM, FUNC_TYPE_SINGLE, "{first value} - {second value}", null,
          this.createFunctParamWithNoName(Array( ("first number type value",  "number", 0), ("second number type value", "number", 0))),
          NumberBasicFunc.getClass),
      "multiple" ->
        this.createFunctWithMethodWithoutPreScript("multiple", FUNC_BY_SYSTEM, FUNC_TYPE_SINGLE, "{first value} * {second value}", null,
          this.createFunctParamWithNoName(Array( ("first number type value",  "number", 0), ("second number type value", "number", 0))),
          NumberBasicFunc.getClass),
      "remainder" ->
        this.createFunctWithMethodWithoutPreScript("remainder", FUNC_BY_SYSTEM, FUNC_TYPE_SINGLE, "{target value} % {decomposer value}", null,
          this.createFunctParamWithNoName(Array( ("target value",  "number", 0), ("decomposer value", "number", 0))),
          NumberBasicFunc.getClass)
    )
  }

  /**
   * @define function의 유형이 system common 인지 아닌지를 판다.
   * */
  def funcTypeByName(funcName : String) : String = {
    try   { this.functList.apply(funcName)._1.by }
    catch { case e : Exception => FUNC_BY_CUSTOMER }
  }

  /**
   * @define
   * */
  def funcTypeByScript(script : String) : String = {
    if(Util.isSingleDeepsFunc(script)){
      funcTypeByName(Util.getFuncName(script))
    }else{
      FUNC_BY_CUSTOMER
    }
  }

  /**
   * === create function param array with no name ===
   *
   * only use system function
   *
   * @param params : array(desc, allow type, init value)
   * */
  def createFunctParamWithNoName(params : Array[(String, String, Any)]) : Traversable[FunctParam] = {
    params.map(f=> { new FunctParam(f._1, f._2, f._3) })
  }

  /**
   * === create function param array with name ===
   *
   * only use script function or customer function
   *
   * @param params : array(name, desc, allow type, init value)
   * */
  def createFunctParam(params : Array[(String, String, String, Any)]) : Traversable[FunctParam] = {
    params.map(f=> { new FunctParam(f._1, f._2, f._3, f._4) })
  }

  /**
   * create funct object with [[Method]]Class
   * */
  def createFunctWithMethodWithoutPreScript
    ( name    : String,
      by      : String,
      type_   : String,
      desc    : String,
      script  : String,
      params  : Traversable[FunctParam],
      cls     : Class[_]  ) : (UnitFunct, Method) = {
    (new UnitFunct(name, by, type_, desc, "", script, params), if(by == FUNC_BY_SYSTEM){getMethodByName(cls, name)}else{null})
  }

  /**
   * create funct object with [[Method]]Class with preScript
   * */
  def createFunctWithMethodAndPreScript
  ( name    : String,
    by      : String,
    type_   : String,
    desc    : String,
    preScript : String,
    script  : String,
    params  : Traversable[FunctParam],
    cls     : Class[_]  ) : (UnitFunct, Method) = {
    (new UnitFunct(name, by, type_, desc, preScript, script, params), if(by == FUNC_BY_SYSTEM){getMethodByName(cls, name)}else{null})
  }

  /**
   * NOTE : by this method, func name not allow duplicate
   * */
  def getMethodByName(cls : Class[_], name : String) : Method = {
    cls.getMethods.filter(f => {f.getName == name})(0)
  }

  def runBasicFunc(funcName : String, param : String*) : Any = {

    val funcContainer = Class.forName("pjt.geon.shine.core.func.basic.number.NumberBasicFunc").newInstance.asInstanceOf[NumberBasicFunc]
    //todo : refactoring point
    val funcParamType = UnitFunct.getMethodByName(NumberBasicFunc.getClass, funcName).getParameterTypes

    // FuCK!!!!!!!!!! SCALA!!!!!!!!!!!!!!!!!!!!!!!!!!!
    val funcParams = param.map(f => { new java.lang.Double(f.toDouble) })

    val ret = Class.forName("pjt.geon.shine.core.func.basic.number.NumberBasicFunc")
      .getDeclaredMethod(funcName, funcParamType:_*)
      .invoke(funcContainer, funcParams:_*)
    ret
  }
}
