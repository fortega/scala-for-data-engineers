package com.github.fortega

import scala.math.Numeric
import scala.util.Try
import org.apache.spark.sql.SparkSession
import scala.annotation.tailrec

case class Mov(id: Short, cliente: String, valor: Double) {
  def split(max: Double): (Mov, Mov) =
    (this.copy(valor = max), this.copy(valor = valor - max))
}
object App {
  def testNameTry: Unit = {
    def divide(i: Int): Double = i / (i - 4)

    val message = NamedTry("create")("initer")
      .map("length")(i => i.length)
      .map("divide")(divide)
      .map("toString")(_.toString)

    message match {
      case Error(name, exception) => println(s"error @ $name: $exception")
      case Success(value)         => println(s"success: $value")
    }
  }

  @tailrec
  def associate(max: Double, compras: List[Mov], accum: List[Mov] = Nil): (List[Mov], List[Mov]) = {
    if(compras.head.valor == max)
      (compras.head :: accum, compras.tail)
    else if(compras.head.valor > max){
      val (movAcum, movRemain) = compras.head.split(max)
      (movAcum :: accum, movRemain :: accum.tail)
    }else
      associate(max - compras.head.valor, compras.tail, compras.head :: accum)
  }

  def process(compras: List[Mov], canjes: List[Mov]): Map[Short, List[Short]] =
    canjes.map { canje =>
      canje.id -> compras.map(_.id)
    }.toMap

  def main(cmdArgs: Array[String]): Unit = {
    val spark = SparkSession.builder
      .master("local[*]")
      .getOrCreate
    import spark.implicits._
    import org.apache.spark.sql.functions._

    val compras = spark
      .range(25)
      .map(i => Mov(i.toShort, s"cliente${(i % 3) + 1}", i * 50))
      .groupBy("cliente")
      .agg(
        collect_list(struct("id", "cliente", "valor")).as("compras")
      )

    val canjes = spark
      .range(5)
      .map(i => Mov(i.toShort, s"cliente${(i % 3) + 1}", 667))
      .groupBy("cliente")
      .agg(
        collect_list(struct("id", "cliente", "valor")).as("canjes")
      )
    val udfProcess = udf(process(_, _))

    val result = compras
      .join(canjes, Seq("cliente"), "left")
      .select(col("cliente"), udfProcess(col("compras"), col("canjes")))

    result.show(10, false)
  }
}
