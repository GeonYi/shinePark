package pjt.geon.shine.core.func.condition

import pjt.geon.shine._
import pjt.geon.shine.command.request.RunActor

import scala.util.control.Breaks._

object Condition {

  def checkConditionSyntax(inArr : Array[(String, String)], by : String) : Any = {

    //todo : add validation check
    def checkIfSyntax(inArr : Array[(String, String)]) : Boolean = {
      if(inArr(0)._1.substring(0,2) != "if" || inArr(inArr.length-1).toString != "endif"){
        false
      }
      else{
        true
      }
    }
    checkIfSyntax(inArr)

    breakable {
      for(i <- inArr.indices){
        if(this.checkCondition(inArr(i)._1, by)){
          return inArr(i)._2
        }
      }
    }
    null
  }

  def checkCondition(inStr : String, by : String) : Boolean = {

    if(inStr == "else"){
      true
    }else{
      val startPoint = inStr.indexOf(SYS_SYNTAX_IF + "(") + 3
      val endPoint   = inStr.length - inStr.reverse.indexOf(")") - 1
      val condScript = inStr.substring(startPoint, endPoint)
      val unitCondition = condScript.split(REG_CONDITION_AND_OR)

      val splitedCondArr : Array[(String,String,String)] =
        unitCondition.map(f => {
          (f.replace(" ", "").split(REG_CONDITION_OPERATOR).apply(0),
            REG_CONDITION_OPERATOR.r.findFirstIn(f).get,
            f.replace(" ", "").split(REG_CONDITION_OPERATOR).apply(1))
        })

      val unitOrAndArr  = REG_CONDITION_AND_OR.r.findAllIn(condScript)
      val unitRelustArr = checkUnitCondition(splitedCondArr, by)

      var trueFalseStr : String = ""

      for(i <- unitRelustArr.indices) yield {
        trueFalseStr = trueFalseStr + unitRelustArr(i) + (if(unitOrAndArr.hasNext){unitOrAndArr.next}else{""})
      }

      while(REG_CONDITION.r.findAllIn(trueFalseStr).nonEmpty){
        trueFalseStr = REG_CONDITION.r.replaceFirstIn(trueFalseStr, condition(REG_CONDITION.r.findFirstIn(trueFalseStr).get))
      }
      if(trueFalseStr == "true"){true}else{false}
    }
  }

  private def checkUnitCondition( splitedCondArr : Array[(String,String,String)], by : String ) : Array[Boolean] = {
    def value(str : String) : Any = {
      if(str.matches(REG_FUNCTION_TYPE)){RunActor.runFunc(str, by)}else str
    }
    splitedCondArr.map(func => {
      val firstValue  = value(func._1).toString
      val secondValue = value(func._3).toString

      func._2 match {
        case "==" => {
          try{ firstValue.toString.toDouble == secondValue.toString.toDouble }
          catch{ case _ => firstValue.toString == secondValue.toString }
        }
        case ">=" => { firstValue.toString.toDouble >= secondValue.toString.toDouble }
        case "<=" => { firstValue.toString.toDouble <= secondValue.toString.toDouble }
        case ">"  => { firstValue.toString.toDouble >  secondValue.toString.toDouble }
        case "<"  => { firstValue.toString.toDouble <  secondValue.toString.toDouble }
        case _    => false
      }
    })
  }

  private def condition(exp : String) : String = {
    exp match {
      case "true||true"     => "true"
      case "true||false"    => "true"
      case "false||true"    => "true"
      case "false||false"   => "false"
      case "true&&true"     => "true"
      case "true&&false"    => "false"
      case "false&&true"    => "false"
      case "false&&false"   => "false"
    }
  }
}
