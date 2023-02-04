package catstypes

import cats.data.{Chain, NonEmptyChain}


object catsCollections {
  //Chain
  // конструкторы
  val empty: Chain[Int] = Chain[Int]()
  val empty2: Chain[Int] = Chain.empty[Int]
  val ch2 = Chain(1)
  val ch3 = Chain.one(1)
  val ch4: Chain[Int] = Chain.fromSeq(1 :: 2 :: 3 :: Nil)

  //операторы
  val ch5 = ch2 :+ 2 // append, const time
  val ch6 = 3 +: ch2 // prepend, const time
  val r = ch2.headOption // только Option, потому что тут не должно быть side эффектов

  ch3.map(_ + 1)
  ch3.flatMap(v => Chain.one(v + 1))

  //NonEmptyChain
  val nec: NonEmptyChain[Int] = NonEmptyChain(1)
  val nec2 = NonEmptyChain.one(1)
  val nec3 = NonEmptyChain.fromSeq(1 :: 2 :: 3 :: Nil)

}
