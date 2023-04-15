package module3

import zio.console.Console
import zio.{Has, ZIO, ZLayer}

import java.io.IOException


package object emailService {

  /**
   * Реализовать Сервис с одним методом sendEmail,
   * который будет принимать Email и отправлять его
   */

  type EmailService = Has[EmailService.Service]

  object EmailService {

    trait Service {
      def sendMail(email: Email): ZIO[Console, IOException, Unit]
    }

    val live = ZLayer.succeed(
      new Service {
        override def sendMail(email: Email): ZIO[Console, IOException, Unit] =
          zio.console.putStrLn(email.toString)
      }
    )

    def sendMail(email: Email): ZIO[EmailService with Console, IOException, Unit] =
      ZIO.accessM(_.get.sendMail(email))

  }


}
