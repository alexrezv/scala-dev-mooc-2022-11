package parser

class MonadicParser[Src, Res](private val parsingFunction: Src => (Src, Res)) {

  def flatMap[T](f: Res => MonadicParser[Src, T]): MonadicParser[Src, T] = MonadicParser {
    parsingFunction(_) match {
      case (src, res) => (src, f(res).parse(src))
    }
  }

  def map[T](f: Res => T): MonadicParser[Src, T] =
    flatMap(res => MonadicParser { src => (src, f(res)) })

  def parse(src: Src): Res = parsingFunction(src)._2

}

object MonadicParser {
  def apply[Src, Res](parsingFunction: Src => (Src, Res)): MonadicParser[Src, Res] =
    new MonadicParser(parsingFunction)
}
