package pjt.geon.shine.core.value

import pjt.geon.shine._

class ValueContainer (name : String = null) extends ValueContainerTrait{
  var valueContainerName = name

  def this() = {
    this("valueContainer_" + java.util.UUID.randomUUID.toString)
  }

  /**
   * remove all value
   * */
  def deleteAllValue() = {this.valueContainer.clear}
}

object ValueContainer{

  var userValueContainer      = this.apply(SYS_VALUE_CONTAINER_USER)
  var functionValueContainer  = this.apply(SYS_VALUE_CONTAINER_FUNC)
  var scriptValueContainer    = this.apply(SYS_VALUE_CONTAINER_SCRIPT)

  /**
   * create new value container
   * */
  def apply(target : String) = {
    new ValueContainer(target)
  }

  def createValue(target : String, typeKeyWord : String, name : String, value : Any) : Unit = {
    containerByName(target).createValue(typeKeyWord, name, value)
  }

  def deleteValue(target : String, name : String) : Unit = {
    containerByName(target).deleteValue(name)
  }

  def getAllValue(target : String) : Array[(String, (String, Any))] = {
    containerByName(target).getValue
  }

  def getValue(target : String, name : String) : (String, Any) = {
    containerByName(target).getValue(name)
  }

  private def containerByName(name : String): ValueContainer ={
    name match {
      case "FunctionValueContainer" => functionValueContainer
      case "ScriptValueContainer"   => scriptValueContainer
      case "UserValueContainer"     => userValueContainer
    }
  }

}
