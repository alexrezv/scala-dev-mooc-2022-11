package module3

import zio.clock.Clock
import zio.console.Console
import zio.duration.durationInt
import zio.random.Random
import zio.{IO, RIO, Task, ZIO}

import scala.language.postfixOps

object di {

  type Query[_]
  type DBError
  type QueryResult[_]
  type Email = String
  type User


  trait DBService {
    def tx[T](query: Query[T]): IO[DBError, QueryResult[T]]
  }

  trait EmailService {
    def makeEmail(email: String, body: String): Task[Email]

    def sendEmail(email: Email): Task[Unit]
  }

  trait LoggingService {
    def log(str: String): Task[Unit]
  }

  trait UserService {
    def getUserBy(id: Int): RIO[LoggingService, User]
  }


  type MyEnv = Random with Clock with Console

  /**
   * Написать эффект который напечатет в консоль приветствие, подождет 5 секунд,
   * сгенерит рандомное число, напечатает его в консоль
   * Console
   * Clock
   * Random
   */

  //    trait Console{
  //      def putStrLn(string: String): UIO[Unit]
  //    }
  //
  //    trait Clock {
  //      def sleep(duration: Duration): UIO[Unit]
  //    }
  //
  //    trait Random{
  //      def nextInt(): UIO[Int]
  //    }


  lazy val e1: ZIO[Random with Clock with Console, Nothing, Unit] = for {
    console <- ZIO.environment[Console].map(_.get)
    clock <- ZIO.environment[Clock].map(_.get)
    random <- ZIO.environment[Random].map(_.get)
    _ <- console.putStrLn("Hello").orDie
    _ <- clock.sleep(5 seconds)
    int <- random.nextInt
    _ <- console.putStrLn(int.toString).orDie
  } yield ()


  lazy val e2: ZIO[MyEnv, Nothing, Unit] = e1

  lazy val getUser: ZIO[LoggingService with UserService, Throwable, User] =
    ZIO.environment[UserService].flatMap(_.getUserBy(10))

  lazy val sendMail: ZIO[EmailService, Throwable, Unit] =
    ZIO.environment[EmailService].flatMap(_.sendEmail("email@a.com"))


  /**
   * Эффект, который будет комбинацией двух эффектов выше
   */
  lazy val combined2:
    ZIO[EmailService with LoggingService with UserService, Throwable, (User, Unit)] = getUser zip sendMail


  /**
   * Написать ZIO программу которая выполнит запрос и отправит email
   */
  lazy val queryAndNotify = ???


  lazy val services: UserService with EmailService with LoggingService = ???

  lazy val dBService: DBService = ???
  lazy val userService: UserService = ???

  lazy val emailService2: EmailService = ???

  def f(userService: UserService): UserService with EmailService with LoggingService = ???

  // provide
  lazy val e3: Task[(User, Unit)] = combined2.provide(services)

  // provide some
  lazy val e4: ZIO[UserService, Throwable, (User, Unit)] = combined2.provideSome[UserService](f)

  // provide
  lazy val e5 = ???

  val f1: Int => Double => String => Unit = ???
  val f2: Double => String => Unit = f1(10)
  val f3: String => Unit = f2(10.0)

}