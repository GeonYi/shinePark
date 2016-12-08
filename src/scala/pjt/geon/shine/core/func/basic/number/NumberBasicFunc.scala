package pjt.geon.shine.core.func.basic.number

import pjt.geon.shine.command.request.Util

class NumberBasicFunc {
  /**
   * int, double type plus(+) operation
   * */
  def plus (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    if(firstNum == java.lang.Double.NaN || secondNum == java.lang.Double.NaN){
      java.lang.Double.NaN
    }else if(Util.checkNumericValue(firstNum) || Util.checkNumericValue(secondNum)){
      (BigDecimal(firstNum) + BigDecimal(secondNum)).toDouble
    } else {
      java.lang.Double.NaN
    }
  }

  /**
   * int, double type minus(-) operation
   * */
  def minus (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    if(firstNum == java.lang.Double.NaN || secondNum == java.lang.Double.NaN){
      java.lang.Double.NaN
    }else if(Util.checkNumericValue(firstNum) || Util.checkNumericValue(secondNum)){
      (BigDecimal(firstNum) - BigDecimal(secondNum)).toDouble
    } else {
      java.lang.Double.NaN
    }
  }

  /**
   * int, double type multiple(*(=:X)) operation
   * */
  def multiple (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    if(firstNum == java.lang.Double.NaN || secondNum == java.lang.Double.NaN){
      java.lang.Double.NaN
    }else if(Util.checkNumericValue(firstNum) || Util.checkNumericValue(secondNum)){
      (BigDecimal(firstNum).*(BigDecimal(secondNum))).toDouble
    } else {
      java.lang.Double.NaN
    }
  }

  def remainder (targetNumber : java.lang.Double, devider : java.lang.Double) : Double = {
    if(targetNumber == java.lang.Double.NaN || devider == java.lang.Double.NaN){
      java.lang.Double.NaN
    }else if(Util.checkNumericValue(targetNumber) || Util.checkNumericValue(devider)){
      (targetNumber % devider)
    } else {
      java.lang.Double.NaN
    }
  }
 }

object NumberBasicFunc {
  def plus (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    new NumberBasicFunc().plus(firstNum, secondNum)
  }

  def minus (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    new NumberBasicFunc().minus(firstNum, secondNum)
  }

  def multiple (firstNum : java.lang.Double, secondNum : java.lang.Double) : Double = {
    new NumberBasicFunc().multiple(firstNum, secondNum)
  }

  def remainder (targetNumber : java.lang.Double, devider : java.lang.Double) : Double = {
    new NumberBasicFunc().remainder(targetNumber, devider)
  }
}