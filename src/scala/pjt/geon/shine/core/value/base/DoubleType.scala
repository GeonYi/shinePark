package pjt.geon.shine.core.value.base

class DoubleType(doubleValue : Any = 0) extends NotNull with Type{

  var value : java.lang.Double = new java.lang.Double(doubleValue.toString)

  def apply(value : Any) = {
    this.value = new java.lang.Double(value.toString)
  }
}

object DoubleType {
  def apply(value : Any) : DoubleType = {
    new DoubleType(value)
  }
}
