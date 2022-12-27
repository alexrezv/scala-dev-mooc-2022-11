package module1

import module1.list.{List, incList, shoutString}
import module1.recursion.fibonacci
import org.scalatest.refspec.RefSpec

class FibonacciSpec extends RefSpec {

  def `fibonacci error`(): Unit = {
    val e = intercept[AssertionError] {
      fibonacci(-1)
    }
    assert(e.getMessage.equals("assertion failed: Number should be greater than 0!"))
  }

  def `fibonacci 0th`(): Unit = {
    assertResult(0)(fibonacci(0))
  }

  def `fibonacci 1st`(): Unit = {
    assertResult(1)(fibonacci(1))
  }

  def `fibonacci 2nd`(): Unit = {
    assertResult(1)(fibonacci(2))
  }

  def `fibonacci 3rd`(): Unit = {
    assertResult(2)(fibonacci(3))
  }

  def `fibonacci 100th`(): Unit = {
    assertResult(BigInt.apply("354224848179261915075"))(fibonacci(100))
  }

}

class ListSpec extends RefSpec {

  def `cons test`(): Unit = {
    assertResult(List(1))(1 :: List.Nil)
    assertResult(List(1, 2))(1 :: List(2))
  }

  def `mkString test`(): Unit = {
    assertResult("")(List.Nil.mkString("|"))
    assertResult("1")(List(1).mkString("|"))
    assertResult("1|2|3")(List(1, 2, 3).mkString("|"))
  }

  def `reverse test`(): Unit = {
    assertResult(List.Nil)(List.Nil.reverse)
    assertResult(List(1))(List(1).reverse)
    assertResult(List(3, 2, 1))(List(1, 2, 3).reverse)
  }

  def `map test`(): Unit = {
    assertResult(List(2, 4, 6))(List(1, 2, 3).map(_ * 2))
    assertResult(List("a!", "b!", "c!"))(List("a", "b", "c").map(_ + "!"))
  }

  def `filter test`(): Unit = {
    assertResult(List(2, 4))(List(1, 2, 3, 4).filter(_ % 2 == 0))
    assertResult(List('A', 'C'))(List('A', 'b', 'C').filter(_.isUpper))
  }

  def `incList test`(): Unit = {
    assertResult(List(3, 4, 5))(incList(List(2, 3, 4)))
  }

  def `shoutString test`(): Unit = {
    assertResult(List("!a", "!b", "!c"))(shoutString(List("a", "b", "c")))
  }

}