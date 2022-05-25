package com.github.fortega

import org.apache.spark.sql.SparkSession

object App {
  private def usingSpark(f: SparkSession => Unit): Unit = {
    val spark = SparkSession.builder
      .master("local[*]")
      .getOrCreate
    f(spark)
    spark.close
  }

  def main(cmdArgs: Array[String]) = usingSpark { spark =>
    import org.apache.spark.sql.functions._
    import spark.implicits._

    val total: Long = Int.MaxValue
    val inside = spark.range(total)
      .select(when(sqrt(pow(rand(), 2) + pow(rand(), 2)) <= 1, 1).otherwise(0))
      .as[Long]
      .reduce(_ + _)
    
      println(s"$inside/$total = ${inside * 4.0 / total} - ${scala.math.Pi}")
  }
}
