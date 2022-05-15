package com.github.fortega

import scala.util.Try

object App {
    def divide(i: Int): Double =  i/(i-4)

    def main(cmdArgs: Array[String]): Unit = {
        val message = NamedTry("create")("initer")
            .map("length")(_.length)
            .map("divide")(divide)
            .map("toString")(_.toString)

        message match {
            case Error(name, exception) => println(s"error @ $name: $exception")
            case Success(value) => println(s"success: $value")
        }
    }
}

