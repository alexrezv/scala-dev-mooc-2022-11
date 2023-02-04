package scala_3.intro

trait Ord[T]:
  def compare(x: T, y: T): Int
  extension (x: T)
    def < (y: T) = compare(x, y) < 0
    def > (y: T) = compare(x, y) > 0

given intOrd: Ord[Int] with
  def compare(x: Int, y: Int) =
    if x < y then -1 else if x > y then +1 else 0

given listOrd[T](using ord: Ord[T]): Ord[List[T]] with

  def compare(xs: List[T], ys: List[T]): Int = (xs, ys) match
    case (Nil, Nil) => 0
    case (Nil, _) => -1
    case (_, Nil) => +1
    case (x :: xs1, y :: ys1) =>
      val fst = ord.compare(x, y)
      if fst != 0 then fst else compare(xs1, ys1)

object scala3test {
  // Ord контекстный параметр
  def max[T](x: T, y: T)(using ord: Ord[T]): T =
    if ord.compare(x, y) < 0 then y else x


  //Во многих ситуациях имя параметра контекста вообще не нужно
  // упоминать явно, поскольку оно используется только в
  // синтезированных аргументах для других параметров контекста.
  // В этом случае можно не задавать имя параметра и
  // указать только его тип. Пример:
  def maximum[T](xs: List[T])(using Ord[T]): T =
  //max принимает контекстный параметр типа Ord[T]
  // только для того, чтобы передать его в качестве
  // выводимого аргумента функции max. Имя параметра опущено.
    xs.reduceLeft(max)

  @main def start()={
    max(2, 3)
    max(List(1, 2, 3), Nil)
  }
}

