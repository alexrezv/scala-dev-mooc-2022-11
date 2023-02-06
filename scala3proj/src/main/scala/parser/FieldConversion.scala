package parser

trait FieldConversion[A, B]:
  def convert(x: A): B

given intFieldConversion: FieldConversion[String, Int] with
  def convert(x: String): Int = x.toInt

given floatFieldConversion: FieldConversion[String, Float] with
  def convert(x: String): Float = x.toFloat

given doubleFieldConversion: FieldConversion[String, Double] with
  def convert(x: String): Double = x.toDouble

given booleanFieldConversion: FieldConversion[String, Boolean] with
  def convert(x: String): Boolean = x.toBoolean