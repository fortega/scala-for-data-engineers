package com.github.fortega

sealed trait NamedTry[+A] {
    def map[B](name: String)(f: A => B): NamedTry[B]
}

object NamedTry {
    def apply[A](name: String)(init: => A): NamedTry[A] = try Success(init) catch {
        case e: Throwable => Error(name, e)
    }
}

case class Success[A](value: A) extends NamedTry[A]{
    def map[B](name: String)(f: A => B) = NamedTry(name)(f(value))
}

case class Error[A](name: String, exception: Throwable) extends NamedTry[A]{
    def map[B](name: String)(f: A => B) = this.asInstanceOf[NamedTry[B]]
}