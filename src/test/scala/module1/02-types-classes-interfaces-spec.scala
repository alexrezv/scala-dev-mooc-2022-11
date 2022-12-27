package module1

import module1.type_system.Rectangle
import org.scalatest.refspec.RefSpec

class RectangleSpec extends RefSpec {

  def `rectangle perimeter test`(): Unit = {
    val rectangle = Rectangle(10.0, 12.0)
    assertResult(44.0)(rectangle.perimeter)
  }

  def `rectangle area test`(): Unit = {
    val rectangle = Rectangle(10.0, 12.0)
    assertResult(120.0)(rectangle.area)
  }

}