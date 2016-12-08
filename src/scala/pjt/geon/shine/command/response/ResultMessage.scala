package pjt.geon.shine.command.response

/**
 * for return value
 */
class Message ( private val returnSucc      : Boolean = false,
                private val returnValue     : Any ,
                private val returnMsg       : String  ) {
  var isSucc  : Boolean = returnSucc
  var msg     : String  = returnMsg
  var value   : Any     = returnValue

  /**
   * === set true return state  ===
   * */
  def setTrue   = {this.isSucc = true}
  def setFalse  = {this.isSucc = false}

  /**
   * === set new message ===
    *
    * @note set new message, not new class(message is not means {{{Message}}} class.
   * @since 2015.09.16
   * */
  def setMsg (message : String) = {this.msg = message}

  def setValue (returnValue : String) = {this.value = returnValue}


  def canEqual(other: Any): Boolean = other.isInstanceOf[Message]

  @Override
  override def equals(other: Any): Boolean = other match {
    case that: Message =>
      (that canEqual this) &&
        isSucc == that.isSucc &&
        msg == that.msg &&
        value == that.value &&
        returnSucc == that.returnSucc &&
        returnValue == that.returnValue &&
        returnMsg == that.returnMsg
    case _ => false
  }

  @Override
  override def hashCode(): Int = {
    val state = Seq(isSucc, msg, value, returnSucc, returnValue, returnMsg)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString =
    s"Message($isSucc, $msg, $value, $returnSucc, $returnValue, $returnMsg, $setTrue, $setFalse, $hashCode)"
}

object Message {

  private var messageList : Seq[Message] = _

  private def addElementList(msg : Message) : Unit = {
    if(messageList == null){
      messageList = Seq(msg)
    }else{
      messageList :+=(msg)
    }
  }

  def newMessage (returnSucc : Boolean, returnValue : Any, returnMsg : String) : Message = {
    val messInstance = new Message(returnSucc, returnValue, returnMsg)
    addElementList(messInstance)
    messInstance
  }
}
 