package module1

import module1.functions.{filterEven, filterOdd, isEven, isOdd}
import org.scalatest.refspec.RefSpec

class FunctionsSpec extends RefSpec {

  def `isEven test`(): Unit = {
    assertResult(false)(isEven(1))
    assertResult(true)(isEven(2))
  }

  def `isOdd test`(): Unit = {
    assertResult(true)(isOdd(1))
    assertResult(false)(isOdd(2))
  }

  def `filterEven test`(): Unit = {
    assertResult(List(0, 2, 4))(filterEven(List(0, 1, 2, 3, 4, 5)))
  }

  def `filterOdd test`(): Unit = {
    assertResult(List(1, 3, 5))(filterOdd(List(0, 1, 2, 3, 4, 5)))
  }

}