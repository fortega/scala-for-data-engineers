package com.github.fortega

import scala.util.Try

object App {
    def main(cmdArgs: Array[String]): Unit = Try(cmdArgs)
        .map(cmdArgToConfig)

    def cmdArgToConfig(cmdArgs: Array[String]) = Config(cmdArgs(0), cmdArgs(1))
}

case class Config(input: String, output: String)

