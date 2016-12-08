
## Shine Park

[[english doc](###eng-head)]

>   *"Shine Park"*을 만든 계기는 단순하다.  [apache Spark](https://spark.apache.org/)을 사용하면서,

> 결국 R과 같은 랭귀지 없이 어떤 수학식을 표현하기란 쉽지 않았다.

> Spark의 api(map, reduce), 그리고 그 이면에 숨어 있는 분산처리의 개념을 수학자가 이해하기란 어려웠다.

> 호기심으로 R 소스를 간단하게 뜯어 보았고, 생각보다는 심플했고, 나도 language를 만들고 싶었다.

> 아무런 프로그램 랭귀지를 만드는데 대한 아무런 배경 지식도 없었으며, proto-type이 목적이었기에,

> 둘째가라면 섭섭한 허술한 프로그램이 완성 되었다.

> "Shine Park"은 한마디로 정의 하자면 **"Spark 엔진에서 동작하는 랭귀지라고 부르기 부끄러운"** 이다.

지금 생각해보자면, 부끄러운 프로그램이지만 이것 또한 추억이며, 누군가에게는 도움이 될 수도 있다는 생각에 공유합니다.

---

### index
01. [development env](####development-env)
02. [basic](####basic)
03. [variable](####variable)
04. [function](####function)
05. [user](####user)

___

#### 01. development env
    java    : 1.8
    spark   : 1.62
    scala   : 2.11.8
    build tool : gradle

#### 02. basic
  "Shine Park"은 기본적으로 2가지 유형과 관련된 명령어만 존재합니다.
  하나는 variable과 관련된 명령어이고, 또 다른 하는 function과 관련된 명령어 입니다.

##### start
 프로그램 시작시 필수적으로 'start($start_name)' 명령어를 입력해야 합니다. 파라미터는 프로젝트의 이름입니다.
 구현은 하지 않았지만, 프로젝트의 이름을 통한 저장기능 등을 구현하기 위해서 받았습니다.

 시작 화면은 다음과 같습니다.

      =============================================
      ShinePark start!!
      =============================================
      >>
 **>>** 뒤에 명령어를 입력할 수 있습니다.

 start(geon_project)이라고 입력하고 \n(엔터키)를 입력하면 다음과 같이 엔진이 초기화 됩니다.

     =============================================
     ShinePark start!!
     =============================================
     >>start(geon_project)
     log4j:WARN No appenders could be found for logger (org.apache.commons.configuration.PropertiesConfiguration).
     log4j:WARN Please initialize the log4j system properly.
     log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
     Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
     16/12/08 08:41:19 INFO SparkContext: Running Spark version 1.6.2
     16/12/08 08:41:20 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
     16/12/08 08:41:20 INFO SecurityManager: Changing view acls to: Geon
     16/12/08 08:41:20 INFO SecurityManager: Changing modify acls to: Geon
     16/12/08 08:41:20 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Set(SDS); users with modify permissions: Set(SDS)
     16/12/08 08:41:21 INFO Utils: Successfully started service 'sparkDriver' on port 59033.
     16/12/08 08:41:22 INFO Slf4jLogger: Slf4jLogger started
     16/12/08 08:41:22 INFO Remoting: Starting remoting
     16/12/08 08:41:22 INFO Remoting: Remoting started; listening on addresses :[akka.tcp://sparkDriverActorSystem@70.70.170.81:59046]
     16/12/08 08:41:22 INFO Utils: Successfully started service 'sparkDriverActorSystem' on port 59046.
     16/12/08 08:41:22 INFO SparkEnv: Registering MapOutputTracker
     16/12/08 08:41:22 INFO SparkEnv: Registering BlockManagerMaster
     16/12/08 08:41:22 INFO DiskBlockManager: Created local directory at C:\Users\Geon\AppData\Local\Temp\blockmgr-8e8cb6f4-b838-40a3-9358-54d3171ac5f9
     16/12/08 08:41:22 INFO MemoryStore: MemoryStore started with capacity 1127.3 MB
     16/12/08 08:41:23 INFO SparkEnv: Registering OutputCommitCoordinator
     16/12/08 08:41:23 INFO Utils: Successfully started service 'SparkUI' on port 4040.
     16/12/08 08:41:23 INFO SparkUI: Started SparkUI at http://70.70.170.81:4040
     16/12/08 08:41:23 INFO Executor: Starting executor ID driver on host localhost
     16/12/08 08:41:23 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 59083.
     16/12/08 08:41:23 INFO NettyBlockTransferService: Server created on 59083
     16/12/08 08:41:23 INFO BlockManagerMaster: Trying to register BlockManager
     16/12/08 08:41:23 INFO BlockManagerMasterEndpoint: Registering block manager localhost:59083 with 1127.3 MB RAM, BlockManagerId(driver, localhost, 59083)
     16/12/08 08:41:23 INFO BlockManagerMaster: Registered BlockManager
     [system print] : Start spark / Name => geon_project

#### 03. variable
##### description
  veriable은 일반적인 프로그래밍에서의 veriable과 거의 유사하게 동작합니다.  기본 타입이라도 내부적으로는 call by reference로 동작합니다.
  추가적인 타입이 아주 많이, 하다못해 String 타입이라도 필요하다는것을 알고 있지만, 구현하지 않았습니다....

  지원 타입은 다음과 같습니다.

  | type name     | key word      | dependency on                  |
  | :-----------: | :-----------: | :----------------------------: |
  | Integer       | int          |  java.lang.Integer |            |
  | Double        | double        | java.lang.Double               |
  | Row           | row           | org.apache.spark.sql.Row       |
  | DataFrame     | df            | org.apache.spark.sql.DataFrame |

##### using

**syntax**

    set value : <type> <var name> = <init value>
    get value : $<var name>

**example :**

    =============================================
    ShinePark start!!
    =============================================
    >>int tmp = 10
    >>$tmp
    [system print] : [(Integer,10)]
    >>int tmp2 = $tmp
    >>$tmp2
    [system print] : [(Integer,10)]
    >>int tmp3 = plus(10,5)
    >>$tmp3
    [system print] : [(Integer,15)]
    >>int tmp4 = plus($tmp, 7)
    >>$tmp4
    [system print] : [(Integer,17)]
    >>double douTmp = 10
    >>$douTmp
    [system print] : [(Double,10.0)]
    >>double douTmp2 = plus(10, $douTmp)
    >>$douTmp2
    [system print] : [(Double,20.0)]
    >>

Row와 Dataframe은 spark의 타입 형태로 일반적인 방법으로 사용할 수 없다.

    >>dataframe dfTmp = load(file, C:\Development\project\shinePark\test_data\temp.txt)

#### 04. function

함수는 반드시 파라미터를 가지며, 여러 기준에 따라 분리된다.

기본적으로 사용 타겟에 따라 시스템과 관련된 **System Function**와 수치 계산과 관련된 일반 함수로 분리할 수 있다.

일반 함수는 다른 함수의 기본이 되는 사칙연산 위주의 **베이스 함수**와 베이스 함수를 이용하는 베이직 함수가 존재한다.

또한 사용자가 정의한 **script 함수** 또한 존재한다.

**syntax**

    <function_name>(<param1>, <param2>, ...)

##### System Function

  | key word      | params                                  | desc                                                                |
  | :-----------: | :-----------                            | :----------------------------------------------------------------  |
  | start         | projectName                             | 새로운 프로젝트를 시작한다                                          |
  | quit          |                                         | 프로젝트를 종료한다                                                 |
  | list          | var                                     | 변수 리스트를 출력한다                                              |
  |               | funcvar                                 | 함수내에서 사용하는 시스템 변수 리스트를 출력한다                   |
  |               | func                                    | 함수 리스트를 출력한다(base function과 script function만 출력한다)  |
  | load          | func                                    | script 함수 파일을 로딩한다                                         |
  |               | filePath                                | 데이터 파일을 load한다(반드시 df변수에 담아야 사용가능하다)         |
  | info          | funcName                                | base함수 또는 script 함수의 정보를 출력한다                         |
  | df            | ("print",varName)                       | dataframe의 내용을 출력한다                                         |
  |               | ("map",varName,targetColumn,script)     | varName(DataFrame)에서 map 작업을 수행한다                          |
  |               | ("reduce",varName,targetColumn,script)  | varName(DataFrame)에서 reduce 작업을 수행한다                       |

**start & quit example :**

     =============================================
     ShinePark start!!
     =============================================
     >>//------------------------------------------------
     >>// **** start new project as geon_project
     >>//------------------------------------------------
     >>start(geon_project)
     log4j:WARN No appenders could be found for logger (org.apache.commons.configuration.PropertiesConfiguration).
     log4j:WARN Please initialize the log4j system properly.
     log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
     Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
     16/12/08 08:41:19 INFO SparkContext: Running Spark version 1.6.2
     16/12/08 08:41:20 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
     16/12/08 08:41:20 INFO SecurityManager: Changing view acls to: Geon
     16/12/08 08:41:20 INFO SecurityManager: Changing modify acls to: Geon
     16/12/08 08:41:20 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Set(SDS); users with modify permissions: Set(SDS)
     16/12/08 08:41:21 INFO Utils: Successfully started service 'sparkDriver' on port 59033.
     16/12/08 08:41:22 INFO Slf4jLogger: Slf4jLogger started
     16/12/08 08:41:22 INFO Remoting: Starting remoting
     16/12/08 08:41:22 INFO Remoting: Remoting started; listening on addresses :[akka.tcp://sparkDriverActorSystem@70.70.170.81:59046]
     16/12/08 08:41:22 INFO Utils: Successfully started service 'sparkDriverActorSystem' on port 59046.
     16/12/08 08:41:22 INFO SparkEnv: Registering MapOutputTracker
     16/12/08 08:41:22 INFO SparkEnv: Registering BlockManagerMaster
     16/12/08 08:41:22 INFO DiskBlockManager: Created local directory at C:\Users\Geon\AppData\Local\Temp\blockmgr-8e8cb6f4-b838-40a3-9358-54d3171ac5f9
     16/12/08 08:41:22 INFO MemoryStore: MemoryStore started with capacity 1127.3 MB
     16/12/08 08:41:23 INFO SparkEnv: Registering OutputCommitCoordinator
     16/12/08 08:41:23 INFO Utils: Successfully started service 'SparkUI' on port 4040.
     16/12/08 08:41:23 INFO SparkUI: Started SparkUI at http://70.70.170.81:4040
     16/12/08 08:41:23 INFO Executor: Starting executor ID driver on host localhost
     16/12/08 08:41:23 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 59083.
     16/12/08 08:41:23 INFO NettyBlockTransferService: Server created on 59083
     16/12/08 08:41:23 INFO BlockManagerMaster: Trying to register BlockManager
     16/12/08 08:41:23 INFO BlockManagerMasterEndpoint: Registering block manager localhost:59083 with 1127.3 MB RAM, BlockManagerId(driver, localhost, 59083)
     16/12/08 08:41:23 INFO BlockManagerMaster: Registered BlockManager
     [system print] : Start spark / Name => geon_project
     >>//------------------------------------------------
     >>// **** end as geon_project
     >>//------------------------------------------------
     >>quit()

**list example :**

    >>//------------------------------------------------
    >>// **** print var list
    >>//------------------------------------------------
    >>int tmp = 10
    >>int tmp2 = 20
    >>list(var)
    -----------------------------------------------
    * var variable value list
    -----------------------------------------------
     (tmp) => (Integer,10)
     (tmp2) => (Integer,20)
    -----------------------------------------------
    >>//------------------------------------------------
    >>// **** print function list
    >>//------------------------------------------------
    >>list(func)
    name : plus
    name : minus
    name : multiple
    name : remainder
    >>

**load example :**

    >>//------------------------------------------------
    >>// **** load new function
    >>//------------------------------------------------
    >>load(func, C:\Development\project\shinePark\func\MultiPlus.func)
    [system print] : C:\Development\project\shinePark\func\MultiPlus.func script load success
    >>
    -----------------------------------------------
    * var variable value list
    -----------------------------------------------
     (tmp) => (Integer,10)
     (tmp2) => (Integer,20)
    -----------------------------------------------
    >>//------------------------------------------------
    >>// **** load file
    >>//------------------------------------------------
    >>dataframe dfTmp = load(file, C:\Development\project\shinePark\test_data\temp.txt)

**info example :**

    >>//------------------------------------------------
    >>// **** print plus function info
    >>//------------------------------------------------
    >>info(plus)
    ==============================================================================
    FUNCTION INFORMATION
    ==============================================================================
    name      : [plus]
    by        : [system]
    type      : [single]
    desc      : [{first value} + {second value}]
    preScript : [
    ]
    script    : [
    null
    ]
    ==============================================================================
    parameter(s) info
    ------------------------------------------------------------------------------
      param name          : [-]
      param desc          : [first number type value]
      param allow type    : [Number]
      param default value : [0]
    ------------------------------------------------------------------------------
      param name          : [-]
      param desc          : [second number type value]
      param allow type    : [Number]
      param default value : [0]
    ------------------------------------------------------------------------------
    ==============================================================================
    >>

**info example :**

    >>//------------------------------------------------
    >>// **** print dataframe
    >>//------------------------------------------------
    >>df(print,dfTmp)
    root
     |-- column_0: string (nullable = true)
     |-- column_1: string (nullable = true)
     +--------+--------+
     |column_0|column_1|
     +--------+--------+
     |     1.0|     1.0|
     |     2.0|     1.0|
     |     3.0|     1.0|
     |     4.0|     1.0|
     |     5.0|     1.0|
     |     6.0|     1.0|
     |     7.0|     1.0|
     |     8.0|     1.0|
     .
     .
     |    48.0|     1.0|
     |    49.0|     1.0|
     |    50.0|     1.0|
     +--------+--------+
    >>
    >>//------------------------------------------------
    >>// **** map dataframe
    >>//------------------------------------------------
    >> dataframe dfTempMap = df(map, dfTmp, column_1, plus(#column_0, #column_1))
    >>df(print, dfTempMap)
    root
     |-- column_0: string (nullable = true)
     |-- column_1: string (nullable = true)
     +--------+--------+
     |column_0|column_1|
     +--------+--------+
     |     1.0|     2.0|
     |     2.0|     3.0|
     |     3.0|     4.0|
     .
     .
     |    47.0|    48.0|
     |    48.0|    49.0|
     |    49.0|    50.0|
     |    50.0|    51.0|
     +--------+--------+


#### 05. user



