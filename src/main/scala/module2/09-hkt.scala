package module2

import scala.language.implicitConversions

object higher_kinded_types {

  def tuple[A, B](a: List[A], b: List[B]): List[(A, B)] =
    a.flatMap { a => b.map((a, _)) }

  def tuple[A, B](a: Option[A], b: Option[B]): Option[(A, B)] =
    a.flatMap { a => b.map((a, _)) }

  def tuple[E, A, B](a: Either[E, A], b: Either[E, B]): Either[E, (A, B)] =
    a.flatMap { a => b.map((a, _)) }


  def tuplef[F[_], A, B](fa: F[A], fb: F[B])
                        (implicit bindA: F[A] => Bindable[F, A], bindB: F[B] => Bindable[F, B]): F[(A, B)] =
    fa.flatMap { a => fb.map((a, _)) }


  trait Bindable[F[_], A] {
    def map[B](f: A => B): F[B]

    def flatMap[B](f: A => F[B]): F[B]
  }

  def tupleBindable[F[_], A, B](fa: Bindable[F, A], fb: Bindable[F, B]): F[(A, B)] = {
    fa.flatMap(a => fb.map(b => (a, b)))
  }


  implicit def optBindable[A](opt: Option[A]): Bindable[Option, A] = new Bindable[Option, A] {
    override def map[B](f: A => B): Option[B] = opt.map(f)

    override def flatMap[B](f: A => Option[B]): Option[B] = opt.flatMap(f)
  }

  implicit def listBindable[A](list: List[A]): Bindable[List, A] = new Bindable[List, A] {
    override def map[B](f: A => B): List[B] = list.map(f)

    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
  }


  val optA: Option[Int] = Some(1)
  val optB: Option[Int] = Some(2)

  val list1 = List(1, 2, 3)
  val list2 = List(4, 5, 6)

  val r3 = println(tupleBindable(optBindable(optA), optBindable(optB)))
  // val r4 = ???

  val r1 = println(tuplef(optA, optB))
  val r2 = println(tuplef(list1, list2))

}