package module3

import zio.console.Console
import zio.{Has, URIO, ZIO, ZLayer}


package object emailService {

  /**
   * Реализовать Сервис с одним методом sendEmail,
   * который будет принимать Email и отправлять его
   */

  type EmailService = Has[EmailService.Service]

  object EmailService {

    trait Service {
      def sendMail(email: Email): URIO[zio.console.Console, Unit]
    }

    val live = ZLayer.succeed(
      new Service {
        override def sendMail(email: Email): URIO[Console, Unit] =
          zio.console.putStrLn(email.toString)
      }
    )

    def sendMail(email: Email): URIO[EmailService with zio.console.Console, Unit] =
      ZIO.accessM(_.get.sendMail(email))

  }


}
