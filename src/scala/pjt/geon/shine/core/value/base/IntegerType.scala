package pjt.geon.shine.core.value.base

class IntegerType(intValue : Any = 0) extends NotNull with Type{

  var value : java.lang.Integer = new java.lang.Integer(intValue.toString)

  def apply(value : Any) = {
    this.value = new java.lang.Integer(value.toString)
  }
}

object IntegerType {
  def apply(value : Any) : IntegerType = {
    new IntegerType(value)
  }
}
