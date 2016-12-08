package pjt.geon.shine.core.func.basic.dataframe

import org.apache.spark.sql.{DataFrame, Row}
import pjt.geon.shine.command.request.RunActor

class DataFrameColumnFunc (  private val dataFrame : DataFrame = null ){
  var df      : DataFrame = dataFrame
}

object DataFrameColumnFunc {
  val VAL_DATAFRAME_CUMULATIVE_VALUE = "~cum"

  private def runPreScript(preScript : String) : Boolean = {
    true
  }

  def dataPrint(df : DataFrame, numRows : Int = 10) : Unit = {
    df.printSchema
    df.show(df.count.toInt)
  }

  /**
   * ~cum: mean cumulative value
   *
   * */
  def reduce(dfName : String, funcScript : String) : Any = {
    val inDf = dataframeByName(dfName : String)
    val dfSchema = inDf.schema

    inDf.rdd.reduce((cumulativeValue , row) => {
      val scriptColumns =
        funcScript.split(Array(',',')','(')).filter(_(0) == '#')

      val scriptColumnValues = scriptColumns.map(f => {
        row.get(dfSchema.fieldIndex(f.substring(1,f.length))).toString
      })

      var convertedScript = funcScript

      scriptColumns.zip(scriptColumnValues).foreach(f => {
        convertedScript = convertedScript.replace(f._1, f._2)
      })
      convertedScript = convertedScript.replace(this.VAL_DATAFRAME_CUMULATIVE_VALUE, cumulativeValue.get(0).toString)
      Row(RunActor.runFunc(convertedScript, "user"))
    })
  }

  /**
   * @param funcScript : started with # is mean column
   * */
  def map(dfName : String, targetColumnName : String, funcScript : String) : DataFrame = {

    val inDf = dataframeByName(dfName : String)
    val index = indexByColumnName(inDf, targetColumnName)
    val dfSchema = inDf.schema

    val resultDf = inDf.map(row => {
      //Array(#aaa,#bbb)
      val scriptColumns =
        funcScript.split(Array(',',')','(')).filter(_(0) == '#')

      val scriptColumnValues = scriptColumns.map(f => {
        row.get(dfSchema.fieldIndex(f.substring(1,f.length))).toString
      })

      //1. find function name
      var convertedScript = funcScript
      scriptColumns.zip(scriptColumnValues).foreach(f => {
        convertedScript = convertedScript.replace(f._1, f._2)
      })

      Row.fromSeq(row.toSeq.patch(index,Seq(RunActor.runFunc(convertedScript, "user").toString),1))
    })

    RunActor.sqlContext.createDataFrame(resultDf, inDf.schema)
  }

  /**
   * === sort
   * */
  def sort(dfName : String, keyColumnName : String) : DataFrame = {
    val inDf = dataframeByName(dfName : String)
    val dfSchema = inDf.schema

    inDf.sort()
  }


  private def indexByColumnName(df : DataFrame, targetColumnName : String) : Int = {
    df.first.fieldIndex(targetColumnName)
  }

  private def dataframeByName(dfName : String) : DataFrame = {
    RunActor.valueFromValueContainer(dfName, "user").asInstanceOf[DataFrame]
  }
}

