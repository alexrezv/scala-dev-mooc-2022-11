package parser

import org.scalatest.refspec.RefSpec


class MonadicParserSpec extends RefSpec {

  def parse[String, B](x: String)(using fc: FieldConversion[String, B]): B = fc.convert(x)


  val StringParser: MonadicParser[String, String] = MonadicParser {
    src => {
      val idx = src.indexOf(";")
      if (idx > -1) (src.substring(idx + 1), src.substring(0, idx))
      else ("", src)
    }
  }

  val IntParser: MonadicParser[String, Int] = StringParser.map(parse)
  val FloatParser: MonadicParser[String, Float] = StringParser.map(parse)
  val BoolParser: MonadicParser[String, Boolean] = StringParser.map(parse)

  def `test 1`() = {
    assertResult("foo")(StringParser.parse("foo"))
    assertResult(42)(IntParser.parse("42"))
    assertResult(4.2f)(FloatParser.parse("4.2"))
    assertResult(true)(BoolParser.parse("true"))
  }

  def `test 2`() = {
    case class Foo(field1: String, field2: Int, field3: Boolean)

    val foo: MonadicParser[String, Foo] = for {
      x <- StringParser
      y <- IntParser
      z <- BoolParser
    } yield Foo(x, y, z)

    assertResult(Foo("foo", 42, true))(foo.parse("foo;42;true"))

    val res: Array[Foo] = "foo;42;true\nbar;228;false\nbuzz;1337;true".split("\n").map(foo.parse)
    assertResult(Array(Foo("foo", 42, true), Foo("bar", 228, false), Foo("buzz", 1337, true)))(res)
  }

  def `test 3`() = {
    case class Car(year: Int, brand: String, model: String, comment: String, price: Float)

    val str = "1997;Ford;E350;ac, abs, moon;3000\n1996;Jeep;Grand Cherokee;MUST SELL! air, moon roof, loaded; 4799"

    val parser =
      for {
        year <- IntParser
        brand <- StringParser
        model <- StringParser
        comment <- StringParser
        price <- FloatParser
      } yield Car(year, brand, model, comment, price)


    val result = str.split('\n').map(parser.parse)
    assertResult(Array(
      Car(1997, "Ford", "E350", "ac, abs, moon", 3000.0f),
      Car(1996, "Jeep", "Grand Cherokee", "MUST SELL! air, moon roof, loaded", 4799.0f)
    ))(result)
  }

}
