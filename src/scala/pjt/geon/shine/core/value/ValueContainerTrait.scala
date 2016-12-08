package pjt.geon.shine.core.value

import pjt.geon.shine._

trait ValueContainerTrait {

  import scala.collection.mutable.{HashMap => scalaHashMap}

  var valueContainer : scalaHashMap[String, Any] = new scalaHashMap[String, Any]

  def createValue(typeKeyWord : String, name : String, value : Any) : Unit = {
    typeKeyWord match {
      // case create by user
      case KEYWORD_VALUE_TYPE_INT       => setValue(name, value.toString.toDouble.toInt)
      case KEYWORD_VALUE_TYPE_ROW       => setValue(name, Row.apply(value.toString))
      case KEYWORD_VALUE_TYPE_DOUBLE    => setValue(name, value.toString.toDouble)
      case KEYWORD_VALUE_TYPE_DATAFRAME => setValue(name, value)

      // case create by system
      case "IntegerType" => setValue(name, value.toString.toInt)
      case "DoubleType"  => setValue(name, value.toString.toDouble)

      // case java.lang.Double or Integer
      case "Integer"    => setValue(name, value.toString.toInt)
      case "Double"     => setValue(name, value.toString.toDouble)

      case _ => println("unusable value type")
    }
  }

  private def setValue(name : String, value : Any ) : Unit = {
    this.valueContainer += (name -> value)
  }

  def deleteValue(name : String) : Unit = {
    this.valueContainer.remove(name)
  }

  def getValue : Array[(String, (String, Any))] = {
    this.valueContainer.map(f => {
      (f._1, (f._2.getClass.getSimpleName, f._2))
    }).toArray
  }

  def getValue(name : String) : (String, Any) = {
    try {
      val rut = this.valueContainer.apply(name)
      (rut.getClass.getSimpleName, rut)
    }
    catch {
      case e : NoSuchElementException => ("no have value : " + name) ; null
    }
  }

  private def existsName(name : String) : Boolean = {
    this.valueContainer.keys.exists(_ == name)
  }

}
