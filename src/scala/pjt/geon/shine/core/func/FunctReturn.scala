package pjt.geon.shine.core.func

class FunctReturn(private val succState : String, private val retCode : String = "", private val retMsg : String = "") extends Return{
  var successState: String = succState
  var returnCode  : String = retCode
  var returnMsg   : String = retMsg
}
