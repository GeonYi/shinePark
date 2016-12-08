package pjt.geon.shine.core.value

import org.apache.spark.sql.{Row => sparkRow}


object Row {
  /**
   * @param inStr row rowName = 10,20,40,30,20
   *
   * */
  def apply(inStr : String) : sparkRow = {
    sparkRow.fromSeq(inStr.split(","))
  }
}

