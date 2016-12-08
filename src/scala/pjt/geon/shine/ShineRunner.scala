package pjt.geon.shine

import pjt.geon.shine.command.request.Runner

/**
  * === Standard In ===
  *
  *  - allow only standard text type input<br>
  *  - first time run "start(empty)"command for run spark
  *
  *  @todo not run command start(empty)
  */
object ShineRunner {

  def main(args : Array[String]) {
    printStartMsg()
    for (ln <- scala.io.Source.stdin.getLines){
      if(ln == null || ln == "" || ln.startsWith("//")){
        // do nothing
        print(">>")
      }else{
        Runner.run(ln, "user")
        print(">>")
      }
    }
  }

  private def printStartMsg() : Unit = {
    println("=============================================")
    println("ShinePark start!!")
    println("=============================================")
    print(">>")
  }
}
