package object monad {

  /**
   * Реализуйте методы map / flatMap / withFilter чтобы работал код и законы монад соблюдались.<br>
   * HINT: для проверки на пустой элемент можно использовать eq
   */

  trait Wrap[+A] {

    def get: A

    private def pure[R](x: R): Wrap[R] = NonEmptyWrap(x)

    def flatMap[R](f: A => Wrap[R]): Wrap[R] = this match {
      case NonEmptyWrap(result) => f(result)
      case _ => EmptyWrap
    }

    // HINT: map можно реализовать через pure и flatMap
    def map[R](f: A => R): Wrap[R] = flatMap(it => pure(f(it)))

    def withFilter(f: A => Boolean): Wrap[A] = flatMap(it => if (f(it)) pure(it) else EmptyWrap)

  }

  object Wrap {
    def empty[R]: Wrap[R] = EmptyWrap
  }

  case class NonEmptyWrap[A](result: A) extends Wrap[A] {
    override def get: A = result
  } // pure

  case object EmptyWrap extends Wrap[Nothing] {
    override def get: Nothing = throw new NoSuchElementException("Wrap.get")
  } // bottom, null element

}