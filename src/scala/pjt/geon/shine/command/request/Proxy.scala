package pjt.geon.shine.command.request

import pjt.geon.shine._

object Proxy {

  /**
   * === set value ===
   * set a value
   *
   * @param typeKeyWord type keyword that (int, double, dataframe, row .......)
   * @param name value name
   * @param isFunc is value by function? "int a = 10" => false, "int a = plus(1,2)" => true
   * @param value value will be set
   * */
  def setValue(typeKeyWord : String, name : String, isFunc : Boolean = false, value : Any , by : String): Unit ={
    RunActor.insertValueContainer(
      typeKeyWord,
      name,
      if(isFunc){RunActor.runFunc(value.toString, by)} else value,
      by
    )
  }

  /**
   * === get value ===
   * get a value
   * 
   * @param name name of value
   * */
  def getValue(name : String, by : String) : Any ={
    println(COMMAND_SYSTEM_OUTPUT
      + CommandActor.valueWithTypeFromValueContainer(name, by).msg)
  }

  /**
   * === run func ===
   * run function
   *
   * @param funcScript script string
   * @example plus(1,2), plus(minus(4,2),1)
   * */
  def runFunc(funcScript : String, by : String) : Any = {
    println(COMMAND_SYSTEM_OUTPUT + RunActor.runFunc(funcScript, by))
  }
}
