package pjt.geon.shine.core.func

/**
 * === function parameter ===
 */
class FunctParam ( private val paramName         : String,
                   private val paramDesc         : String,
                   private val paramAllowType    : Any,
                   private val paramDefaultValue : Any = null){

  def this (paramDesc : String, paramAllowType : Any, paramDefaultValue : Any) = {
    this("-", paramDesc, paramAllowType, paramDefaultValue)
  }

  def this (paramDesc : String, paramAllowType : Any) = {
    this("-", paramDesc, paramAllowType, null)
  }

  //number include int, double
  object DataType extends Enumeration(){
    type DataType = Value
    val Number, Int, Double = Value
  }

  var name        : String = paramName
  val desc        : String = paramDesc
  val allowType   : DataType.Value = {

    paramAllowType.toString.toLowerCase match {
      case "number" => DataType.Number
      case "int"    => DataType.Int
      case "double" => DataType.Double
    }
  }

  var defaultVal  : Any   = paramDefaultValue
}
