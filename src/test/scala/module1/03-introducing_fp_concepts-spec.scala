package module1

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