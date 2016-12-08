package pjt.geon.shine.core.func.basic.dataframe

import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{DataFrame, Row}

class SpDataFrame (df : DataFrame, dfName : String) {

  var dataframe           : DataFrame = df
  var dataframeTableName  : String    = dfName

  private var isRegTable : Boolean = false
  
  private def regTable = {
    dataframe.registerTempTable(dataframeTableName)
    this.isRegTable = true
  }

  // ------------------------------------------------------------
  // dataframe property
  // ------------------------------------------------------------
  // length
  // sum, min, max, avg
  // ------------------------------------------------------------
  lazy val length : Long = dataframe.rdd.count

//  def sum(columnName : String) : Double = {
//    this.dataframe.
//  }

//  private val getReduceRow : (Row, Row, Row) =
//    if(getReduceRow == null){
//      this.reducedRow
//    }else{
//      getReduceRow
//    }

  lazy val sum : Row  = {
    df.rdd.reduce((x, y) => {
      Row.fromSeq(
        for(i <- 0 until df.schema.length) yield {
          if(df.schema.apply(i).dataType == DoubleType) {
            x.getDouble(i) + y.getDouble(i)
          }
          else {
            Double.NaN
          }
        } //end of for syntax
      )
    })
  }

  lazy val min  : Row = {
    df.rdd.reduce((x, y) => {
      Row.fromSeq(
        for(i <- 0 until df.schema.length) yield {
          if(df.schema.apply(i).dataType == DoubleType) {
            x.getDouble(i).min(y.getDouble(i))
          }
          else {
            null
          }
        } //end of for syntax
      )
    })
  }

  lazy val max  : Row = {
    df.rdd.reduce((x, y) => {
      Row.fromSeq(
        for(i <- 0 until df.schema.length) yield {
          if(df.schema.apply(i).dataType == DoubleType) {
            x.getDouble(i).max(y.getDouble(i))
          }
          else {
            null
          }
        } //end of for syntax
      )
    })
  }

  object DataFrameProp {
//    private def checkHasColumn (df : DataFrame, columnName : String) = {
//      try{df.schema.fieldIndex(columnName)}
//      catch{new Exception => println("no have " +  columnName + " dataframe")}
//    }

    def columnSum(df : DataFrame, column : String) : java.lang.Double = {

      df.select(column).rdd.reduce((x, y) => {
        Row.fromSeq(Seq(x.getDouble(0) + y.getDouble(0)))
      }).getDouble(0)
    }
  }
}
