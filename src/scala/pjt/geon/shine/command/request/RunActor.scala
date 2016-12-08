package pjt.geon.shine.command.request

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import pjt.geon.shine._
import pjt.geon.shine.core.func.basic.dataframe.DataFrameColumnFunc
import pjt.geon.shine.core.func.script.Script
import pjt.geon.shine.core.func.{FunctReturn, UnitFunct}
import pjt.geon.shine.core.spark
import pjt.geon.shine.core.value.ValueContainer

object RunActor extends scala.runtime.Nothing$ with Serializable {

  var sparkContext  : SparkContext = _
  var sqlContext    : SQLContext   = _

  val sysCommonFuncKeyWordSet =
    Array(
      KEYWORD_FUNC_START,
      KEYWORD_FUNC_QUIT,
      KEYWORD_FUNC_INFO,
      KEYWORD_FUNC_LIST,
      KEYWORD_FUNC_LOAD,
      KEYWORD_FUNC_DATAFRAME
    )

  /**
   * make Spark Context using spark conf.
   *
   * @see spark [[SQLContext]]
   * */
  private def initHeartRunEnv(appName : String) : Unit = {
    //create spark context
    this.sparkContext = spark.SparkContext.makeSparkContext(appName)

    //create sql context
    this.sqlContext = new org.apache.spark.sql.SQLContext(sparkContext)
  }

  /**
   * create new table if not exist
   *
   * */
  def createTable(tableName : String) = {

  }

  /**
   * insert value to "Value Container"
   * */
  def insertValueContainer(typeKeyWord : String, name : String, value : Any, by : String) : Unit = {
    ValueContainer.createValue(
      if(by == "user"){SYS_VALUE_CONTAINER_USER}else{SYS_VALUE_CONTAINER_SCRIPT},
      typeKeyWord,
      name,
      if(value.toString.charAt(0) == '$'){valueFromValueContainer(value.toString.substring(1, value.toString.length), by)}else{value}
    )
  }

  /**
   * get value from "Value Container"
   * */
  def valueFromValueContainer(typeKeyWord : String, by : String) : Any = {
    ValueContainer.getValue(valueContainerByUserType(by), typeKeyWord)._2
  }

  /**
   * get value with type from "Value Container"
   * */
  def valueWithTypeFromValueContainer(typeKeyWord : String, by : String) : Any = {
    by match {
      case "user"   => ValueContainer.getValue(valueContainerByUserType(by), typeKeyWord.substring(1,typeKeyWord.length))
      case "system" => this.valueFromValueContainer(typeKeyWord.substring(1,typeKeyWord.length),by)
      case _        => println("user type miss match"); null
    }
  }

  /**
   * get all value with type from "Value Container"
   * */
  def valuesFromValueContainer(by : String) : Array[(String, (String, Any))] = {
    ValueContainer.getAllValue(by)
  }

  /**
   * get all function"
   * */
  def functFromFuncContainer() : Iterable[String]  = {
    UnitFunct.functList.map(f => {
      f._2._1.name
    })
  }

  /** get function info */
  def printFunctionInfo(funcName : String) : Unit = {
    UnitFunct.functList.apply(funcName)._1.println
  }

  /**
   * run function
    *
    * @note : add => if customer function, funcName = funcScript
   * */
  def runFunc(funcScript : String, by : String) : Any = {

    var returnRef : Any = null

    //system common function check
    if(isSysCommonFuncKeyword(funcScript)){
      val functType = Util.funcType(funcScript)
      returnRef = runSystemFunc(functType._1, functType._2:_*)
    }
    else{
      returnRef = runScriptFuncWithinBasicFunc(by, setParemeters)
    }

    def isSysCommonFuncKeyword(keyWord : String) : Boolean = {
      this.sysCommonFuncKeyWordSet.contains(keyWord) || this.sysCommonFuncKeyWordSet.contains(keyWord.substring(0,
        if(keyWord.indexOf("(") == -1){keyWord.length}else{keyWord.indexOf("(")}
      ))
    }

    def setParemeters : String = {
      "\\$[a-zA-Z]{1,}[a-zA-Z0-9]{0,49}".r.replaceAllIn(funcScript, s => {
        var tmp = s.toString
        tmp = tmp.substring(1, tmp.length)
        valueFromValueContainer(tmp, by).toString
      })
    }

    returnRef
  }

  /**
   * include basic function, script function, custom script function
   *
   * only allow script type without params : plus(1,2)
   * */
  def runScriptFuncWithinBasicFunc(by:String, funcScript : String) : Any = {

    val funcNameWithParams = Util.funcType(funcScript)
    val functType = UnitFunct.funcTypeByScript(funcScript)

//    val isSingleFunc = if(funcScript.count(_ == "(") == 1){true} else {false}

    // if basic function => "+, -, *, %, ......"
    if(functType == FUNC_BY_SYSTEM){
      UnitFunct.runBasicFunc(funcNameWithParams._1, funcNameWithParams._2:_*)
    }
    else if(functType == FUNC_BY_SCRIPT){
      val isSingleOrMulti : String =
        try{
          UnitFunct.functList.apply(funcNameWithParams._1)._1.type_
        }catch{
          case _ => FUNC_TYPE_SINGLE
        }

      if(isSingleOrMulti == FUNC_TYPE_MULTI){
        Script.runMultiScript(funcScript)
      }else{
        Script.runScriptFunct(by, funcNameWithParams._1, funcNameWithParams._2:_*)
      }
    } else {
      // customer script
      Script.runScriptFunct(by, funcScript)
    }
  }

  def runSingleScriptFuncWithinBasicFuncWithParams(by:String, funcName:String, params:String*) : Any = {
    val functType = UnitFunct.funcTypeByName(funcName)

    // if basic function => "+, -, *, %, ......"
    if(functType == FUNC_BY_SYSTEM){
      UnitFunct.runBasicFunc(funcName, params:_*)
    }
    else if(functType == FUNC_BY_SCRIPT){
      Script.runScriptFunct(by, funcName, params:_*)
    }
  }

  def runSystemFunc(funcName : String, params : String*) : Any = {
    funcName match {
      case KEYWORD_FUNC_START => {
        this.initHeartRunEnv(params(0))
        "Start spark / Name => " + params(0)
      }
      case KEYWORD_FUNC_QUIT  => System.exit(0)
      case KEYWORD_FUNC_INFO  => printFunctionInfo(params(0))
      case KEYWORD_FUNC_LIST  => {
        if(params(0) == KEYWORD_FUNC_LIST_PARAM_VAR || params(0) == KEYWORD_FUNC_LIST_PARAM_FUNCVAR){
          println("-----------------------------------------------")
          println("* " + params(0) + " variable value list")
          println("-----------------------------------------------")
          valuesFromValueContainer(
            if(params(0) == KEYWORD_FUNC_LIST_PARAM_VAR){
              SYS_VALUE_CONTAINER_USER
            }else{
              SYS_VALUE_CONTAINER_SCRIPT}
            ).foreach(f => {
            println(" (" + f._1 + ") => (" + f._2._1 + "," + f._2._2 + ")")
          })
          println("-----------------------------------------------")
          // "value print success"
        }
        else if(params(0) == KEYWORD_FUNC_LIST_PARAM_FUNC){
          functFromFuncContainer.foreach(f => {
            println("name : " + f.toString)
          })
        }else{
          "wrong " + KEYWORD_FUNC_LIST + " syntax"
        }
      }

      case KEYWORD_FUNC_LOAD  => {
        if(params(0) == KEYWORD_FUNC_LOAD_PARAM_FUNC){ //first param
          Script.loadFuncFromFile(params(1))
          params(1) + " script load success"
        }
        else if(params(0) == KEYWORD_FUNC_LOAD_PARAM_FILE){
          val rdd : RDD[Row] = sparkContext.textFile(params(1)).map(_.split(",")).map(row => Row.fromSeq(row))
          val columnArray = (for(i <- 0 until rdd.first.size) yield {"column_" + i}).toArray
          val schema : StructType = StructType(columnArray.map(fieldName => StructField(fieldName, StringType, true)))
          sqlContext.createDataFrame(rdd, schema)
        }
      }
      case KEYWORD_FUNC_DATAFRAME => {
        if(params(0) == KEYWORD_FUNC_DATAFRAME_PARAM_PRINT){
          DataFrameColumnFunc.dataPrint(valueFromValueContainer(params(1), "user").asInstanceOf[DataFrame])
          "Dataframe print success"
        }
        else if(params(0) == KEYWORD_FUNC_DATAFRAME_PARAM_MAP){
          //run outer script
          Script.runOuterScript(params(3))

          DataFrameColumnFunc.map(
            params(1),  // dataframe name
            params(2),  // dataframe replace target column
            params(3)   // function script
          )
        }
        else if(params(0) == KEYWORD_FUNC_DATAFRAME_PARAM_REDUCE){
          //run outer script
          Script.runOuterScript(params(2))

          DataFrameColumnFunc.reduce(
            params(1),  // dataframe name
            params(2)   // function script
          )
        }
      }
      case _ => new FunctReturn("false", "", "")
    }
  }

  private def valueContainerByUserType(by : String) : String = {
    if(by == "system"){SYS_VALUE_CONTAINER_SCRIPT}
    else if(by == "user"){SYS_VALUE_CONTAINER_USER}
    else{null}
  }
}
