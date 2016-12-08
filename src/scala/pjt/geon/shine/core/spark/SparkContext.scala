package pjt.geon.shine.core.spark

import org.apache.spark.SparkContext

object SparkContext {
  def makeSparkContext(appName : String) : SparkContext = {
    new SparkContext(ConfProperties.makeSparkConf(appName))
  }
}
