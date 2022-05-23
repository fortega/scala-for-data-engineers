scalaVersion := "2.12.15"

name := "scala-for-data-engineer"

organization := "com.github.fortega"

version := "0.0.0"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % "3.2.1",
    "org.scalatest" %% "scalatest" % "3.2.11" % Test,
)