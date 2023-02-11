package scala_3.extendingclasses

import scala_3.extendingclasses.Extensionmethods.Circle


object homework1 {
  extension (x: String)
    private def :+:(y: String): Int = x.toInt + y.toInt

  @main def part1Ex(): Unit = {
    println("1" :+: "33")  // 34
  }
}

object homework2 {

  enum CompletionArg:
    case ShowItIsString(s: String)
    case ShowItIsInt(i: Int)
    case ShowItIsFloat(f: Float)

  private object CompletionArg:
    given fromString: Conversion[String, CompletionArg] = ShowItIsString(_)

    given fromInt: Conversion[Int, CompletionArg] = ShowItIsInt(_)

    given fromFloat: Conversion[Float, CompletionArg] = ShowItIsFloat(_)
  end CompletionArg


  import CompletionArg.*

  private def complete(arg: CompletionArg): String = arg match
    case ShowItIsString(s) => s"It's a string: $s"
    case ShowItIsInt(i) => s"It's an Int: $i"
    case ShowItIsFloat(f) => s"It's a Float: $f"

  @main def part2Ex(): Unit = {
    println(complete("String"))
    println(complete(1))
    println(complete(7.7f))
  }

}


object homework3 {
  opaque type Logarithm = Double

  object Logarithm:
    def apply(d: Double): Logarithm = math.log(d)

    def safe(d: Double): Option[Logarithm] =
      if d > 0.0 then Some(math.log(d)) else None

  end Logarithm


  extension (x: Logarithm)
    def toDouble: Double = math.exp(x)
    def +(y: Logarithm): Logarithm = Logarithm(math.exp(x) + math.exp(y))
    def *(y: Logarithm): Logarithm = x + y

}

@main def part3Ex(): Unit = {
  import homework3.Logarithm

  val l: Logarithm = Logarithm(1.0)
  val l2: Logarithm = Logarithm(2.0)
  val l3: Logarithm = l * l2
  val l4: Logarithm = l + l2

  println(l3.toDouble)
  println(l4.toDouble)

}
