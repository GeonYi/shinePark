package pjt.geon.shine.core.spark

import java.io.File

import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.spark.SparkConf
import pjt.geon.shine._

object ConfProperties {

  private lazy val PROPERTIES_FILE_NAME = "spark_conf.properties"

  private def initConfFromPropertiesFile() : scala.collection.immutable.Map[String, String] = {

    val filePath : String = new File("").getAbsolutePath + "\\" + SPARK_CONF_DIRECTORY_NAME + "\\" +
      this.PROPERTIES_FILE_NAME
    val confProper = new PropertiesConfiguration(filePath)
    Map(
      "master" -> confProper.getProperty("master").toString,
      "executor.memory" -> confProper.getProperty("executor.memory").toString
    )
  }

  def makeSparkConf(appName : String) : SparkConf = {
    val properties = initConfFromPropertiesFile()

    val newAppName = {
      if(appName == null || appName == ""){"park bit-na"} else appName
    }

    new SparkConf()
      //todo : to be change,
      .setMaster(properties("master"))
      .set("spark.executor.memory",properties("executor.memory"))
      .setAppName(newAppName)
  }
}
