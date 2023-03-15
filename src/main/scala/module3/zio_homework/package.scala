package module3

import module3.zio_homework.config.AppConfig
import zio.clock.Clock
import zio.console.{Console, _}
import zio.duration.durationInt
import zio.random._
import zio.{Has, Layer, RIO, Schedule, URIO, ZIO, ZLayer, clock}

import java.util.concurrent.TimeUnit
import scala.language.postfixOps

package object zio_homework {
  /**
   * 1.
   * Используя сервисы Random и Console, напишите консольную ZIO программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в когнсоль угадал или нет. Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */

  lazy val readIntOrRetry: RIO[Console, Int] =
    (getStrLn >>= (str => ZIO.effect(str.toInt)))
      .orElse(ZIO.effect(println("Wrong input!")) zipRight readIntOrRetry)

  lazy val guessProgram: RIO[Console with Random, Unit] = for {
    _ <- putStrLn("Try to guess a random number between 1 and 3!")
    number <- readIntOrRetry
    random <- nextIntBetween(1, 4)
    _ <- putStrLn("Success!").when(random == number)
    _ <- putStrLn("Fail!").when(random != number)
  } yield ()

  /**
   * 2. реализовать функцию doWhile (общего назначения), которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   *
   */

  def doWhile[R, E, A](effect: ZIO[R, E, A], predicate: A => Boolean): ZIO[R with Clock, E, A] =
    effect.repeat(Schedule.recurWhile(predicate))

  /**
   * 3. Реализовать метод, который безопасно прочитает конфиг из файла, а в случае ошибки вернет дефолтный конфиг
   * и выведет его в консоль
   * Используйте эффект "load" из пакета config
   */


  def loadConfigOrDefault: URIO[Console, AppConfig] =
    config.load
      .tapError(e => putStrLn(e.getMessage()))
      .orElse(for {
        default <- ZIO.effectTotal(AppConfig("0.0.0.0", "8080"))
        _ <- putStrLn(s"Fallback to the default: $default")
      } yield default)


  /**
   * 4. Следуйте инструкциям ниже для написания 2-х ZIO программ,
   * обратите внимание на сигнатуры эффектов, которые будут у вас получаться,
   * на изменение этих сигнатур
   */


  /**
   * 4.1 Создайте эффект, который будет возвращать случайеым образом выбранное число от 0 до 10 спустя 1 секунду
   * Используйте сервис zio Random
   */
  lazy val eff: URIO[Random with Clock, Int] =
    ZIO.sleep(1 second) zipRight nextIntBetween(0, 11)

  /**
   * 4.2 Создайте коллукцию из 10 выше описанных эффектов (eff)
   */
  lazy val effects: Iterable[URIO[Random with Clock, Int]] =
    ZIO.replicate(10)(eff)


  /**
   * 4.3 Напишите программу которая вычислит сумму элементов коллекци "effects",
   * напечатает ее в консоль и вернет результат, а также залогирует затраченное время на выполнение,
   * можно использовать ф-цию printEffectRunningTime, которую мы разработали на занятиях
   */

  lazy val app: URIO[Clock with Console with Random, Int] =
    zioConcurrency.printEffectRunningTime(
      for {
        result <- ZIO.reduceAll(ZIO.effectTotal(0), effects)(_ + _)
        _ <- putStrLn(s"The result is: $result")
      } yield result
    )


  /**
   * 4.4 Усовершенствуйте программу 4.3 так, чтобы минимизировать время ее выполнения
   */

  lazy val appSpeedUp: URIO[Clock with Console with Random, Int] =
    zioConcurrency.printEffectRunningTime(
      for {
        result <- ZIO.reduceAllPar(ZIO.effectTotal(0), effects)(_ + _)
        _ <- putStrLn(s"The result is: $result")
      } yield result
    )


  /**
   * 5. Оформите ф-цию printEffectRunningTime разработанную на занятиях в отдельный сервис, так чтобы ее
   * молжно было использовать аналогично zio.console.putStrLn например
   */

  object runningTimePrinter {
    type RunningTimePrinter = Has[RunningTimePrinter.Service]

    object RunningTimePrinter {
      trait Service {
        def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[Clock with R, E, A]
      }

      object Service {
        private def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[Clock with R, E, A] = for {
          start <- clock.currentTime(TimeUnit.SECONDS)
          r <- zio
          end <- clock.currentTime(TimeUnit.SECONDS)
          _ <- ZIO.effect(println(s"Running time ${end - start}")).orDie
        } yield r

        val live: Service = new Service {
          override def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[Clock with R, E, A] =
            Service.printEffectRunningTime(zio)
        }

      }

      val live: Layer[Nothing, RunningTimePrinter] =
        ZLayer.succeed(Service.live)
    }

    def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[RunningTimePrinter with Clock with R, E, A] =
      ZIO.accessM(_.get printEffectRunningTime zio)

  }


  /**
   * 6.
   * Воспользуйтесь написанным сервисом, чтобы созадть эффект, который будет логировать время выполнения прогаммы из пункта 4.3
   *
   *
   */

  import module3.zio_homework.runningTimePrinter.{RunningTimePrinter, printEffectRunningTime}

  lazy val appWithTimeLogg: URIO[Clock with Console with Random with RunningTimePrinter, Int] =
    printEffectRunningTime(app)

  /**
   *
   * Подготовьте его к запуску и затем запустите воспользовавшись ZioHomeWorkApp
   */

  lazy val runApp: URIO[Clock with Console with Random, Int] =
    appWithTimeLogg.provideSomeLayer[Clock with Console with Random](RunningTimePrinter.live)

}
