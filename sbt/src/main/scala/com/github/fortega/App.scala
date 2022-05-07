package com.github.fortega

import scala.util.Try

object App {
    def main(cmdArgs: Array[String]): Unit = {
        val message = NamedTry("create")("init")
            .map("length")(_.length)
            .map("divide"){ i =>
                val a = 0/(4-i)
                a
            }
        println(message)
        
    }
}

