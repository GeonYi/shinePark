package pjt.geon

package object shine {

  final val COMMAND_SYSTEM_OUTPUT = "[system print] : "
  final val PARAM_SEP         = ","
  final val COMMAND_START     = "start"
  final val COMMAND_QUIT      = "quit"
  final val COMMAND_VALUE     = "value"
  final val COMMAND_FUNC      = "func"
  final val COMMAND_OPT_INFO  = "info"
  final val COMMAND_SCRIPT    = "script"
  final val COMMAND_DATAFRAME = "df"

  /**
    * start with [a-z] or [A-Z] or some special charter($ _ %)
    * can't over 50 charters
    * _ : customer script make value
    * % : system make value
    * */
  final val REG_SYSTEM_VALUE_TYPE       = "[a-zA-Z_%]{0,}[a-zA-Z0-9]{1,49}"

  /**
    * start with [a-z] or [A-Z]
    * $ : var call
    * _ : customer script make value
    * % : system make value
    * */
  final lazy val REG_USER_VALUE_TYPE      = "\\$[a-zA-Z]{0,}[a-zA-Z0-9]{1,49}"
  final lazy val REG_CREATE_VALUE_TYPE    = "[a-zA-Z]{1,}[a-zA-Z0-9]{0,49}"
  final lazy val REG_FUNCTION_TYPE        = "^[a-zA-Z]{2,}[(].*[)]"

  final lazy val REG_CONDITION_AND_OR     = "[|||&]{2}"
  final lazy val REG_CONDITION_OPERATOR   = "==|>=|<=|>|<"
  final lazy val REG_CONDITION            = "(false|true)[\\&\\|]{2}(false|true)"


  // ====================================================
  // system syntax
  // ====================================================
  val SYS_SYNTAX_IF = "if"

  var SYS_VALUE_CONTAINER_FUNC    = "FunctionValueContainer"
  var SYS_VALUE_CONTAINER_SCRIPT  = "ScriptValueContainer"
  var SYS_VALUE_CONTAINER_USER    = "UserValueContainer"

  // ====================================================
  // spark
  // ====================================================
  val SPARK_CONF_DIRECTORY_NAME = "conf"
  val SCALA_NEW_LINE            = "\n"

  // ====================================================
  // type keyword
  // ====================================================
  val KEYWORD_VALUE_TYPE_INT         = "int"
  val KEYWORD_VALUE_TYPE_ROW         = "row"
  val KEYWORD_VALUE_TYPE_DATAFRAME   = "dataframe"
  val KEYWORD_VALUE_TYPE_DOUBLE      = "double"

  // promised value for system
  val SYS_INTEGER_TYPE        = "IntegerType"
  val SYS_DOUBLE_TYPE         = "DoubleType"
  val SYS_TMP_VAL_NAME_PREFIX = "_sys_tmp_value_"
  val SYS_DF_PROP_PREFIX      = "prop"

  // ====================================================
  // function keyword
  // ====================================================
  val FUNC_PARAM_SEP = ","

  val FUNC_BY_SYSTEM      = "system"
  val FUNC_BY_CUSTOMER    = "customer"
  val FUNC_BY_SCRIPT      = "script"

  // function type about create
  val FUNC_TYPE_SINGLE    = "single"
  val FUNC_TYPE_MULTI     = "multi"
  val FUNC_TYPE_MULTI_INNER = "inner"
  val FUNC_TYPE_MULTI_OUTER = "outer"

  // function reserved value name
  val FUNC_RESERVED_VALUE_DOUBLE  = "r2suitD0ubie"


  // ====================================================
  // system function & params
  // ====================================================
  // info
  val KEYWORD_FUNC_INFO               = "info"

  // list
  val KEYWORD_FUNC_LIST               = "list"
  val KEYWORD_FUNC_LIST_PARAM_VAR     = "var"
  val KEYWORD_FUNC_LIST_PARAM_FUNCVAR = "funcvar"
  val KEYWORD_FUNC_LIST_PARAM_FUNC    = "func"

  // start
  val KEYWORD_FUNC_START              = "start"

  // start
  val KEYWORD_FUNC_QUIT               = "quit"

  // load function & param
  val KEYWORD_FUNC_LOAD               = "load"
  val KEYWORD_FUNC_LOAD_PARAM_FUNC    = "func"
  val KEYWORD_FUNC_LOAD_PARAM_FILE    = "file"

  // dataframe function & param
  val KEYWORD_FUNC_DATAFRAME              = "df"
  val KEYWORD_FUNC_DATAFRAME_PARAM_PRINT  = "print"
  val KEYWORD_FUNC_DATAFRAME_PARAM_MAP    = "map"
  val KEYWORD_FUNC_DATAFRAME_PARAM_REDUCE = "reduce"
}
