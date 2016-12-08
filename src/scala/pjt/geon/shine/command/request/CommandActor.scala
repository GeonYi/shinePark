package pjt.geon.shine.command.request

import pjt.geon.shine.command.response.Message

object CommandActor {

  def insertValueContainer(typeKeyWord : String, name : String, value : Any, by : String) : Message = {
    try{
      RunActor.insertValueContainer(typeKeyWord, name, value, by)
      Message.newMessage(true, null, "create new value '" + value + "' named '" + name +"'")
    }
    catch{
      case e : Exception => Message.newMessage(false, null, "create value failed. check command.")
    }
  }

  def valueFromValueContainer(typeKeyWord : String, by : String) : Message = {
    try{
      val value = RunActor.valueFromValueContainer(typeKeyWord, by)
      Message.newMessage(true, value, "get value " + value)
    }
    catch{
      case e : Exception => Message.newMessage(false, null, "create value fail.  check command.")
    }
  }

  def valueWithTypeFromValueContainer(name : String, by : String) : Message = {
    try{
      val value = RunActor.valueWithTypeFromValueContainer(name, by)
      Message.newMessage(true, value, "[" + value + "]")
    }
    catch{
      case e : Exception => Message.newMessage(false, null, "get value fail.")
    }
  }

  def valuesFromValueContainer(by : String) : Message = {
    try{
      Message.newMessage(true, RunActor.valuesFromValueContainer(by), "all value by " + by)
    }
    catch{
      case e : Exception => Message.newMessage(false, null, "get values fail.  check command.")
    }
  }

  def functFromFuncContainer() : Message = {
    try{
      Message.newMessage(true, RunActor.functFromFuncContainer, "get all function success")
    }
    catch{
      case e : Exception => Message.newMessage(false, null, "get values fail.  check command.")
    }
  }

  def functionInfo(funcName : String) : Message = {
    try{
      Message.newMessage(true, RunActor.printFunctionInfo(funcName), "print function information")
    }catch{
      case e : Exception => Message.newMessage(false, null, "fail to print function information.")
    }
  }

  def runFunc(funcScript : String, by : String) : Any = {
    try{
      Message.newMessage(true, RunActor.runFunc(funcScript, by), "run function success")
    }catch{
      case e : Exception => Message.newMessage(false, null, "fail to run runction")
    }
  }
}
