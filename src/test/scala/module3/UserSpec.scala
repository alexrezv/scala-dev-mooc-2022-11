package module3

import module3.emailService.{Email, EmailAddress, EmailServiceMock, Html}
import module3.userDAO.UserDAOMock
import module3.userService.{User, UserID, UserService}
import zio.console.Console
import zio.test.Assertion.{anything, equalTo}
import zio.test.environment.TestConsole
import zio.test.mock.Expectation.{unit, value}
import zio.test.{DefaultRunnableSpec, assertM, suite, testM}

object UserSpec extends DefaultRunnableSpec {
  override def spec = suite("User spec") {
    testM("notify user") {

      val sendMailMock = EmailServiceMock.SendMail(
        equalTo(Email(EmailAddress("test@test.com"), Html("Hello here"))), unit
      )

      val daoMock = UserDAOMock.FindBy(
        equalTo(UserID(1)), value(Some(User(UserID(1), EmailAddress("test@test.com"))))
      )

      val layer = daoMock >>> UserService.live ++ sendMailMock

      assertM(UserService.notifyUser(UserID(1))
        .provideSomeLayer[TestConsole with Console](layer))(anything)
    }
  }
}
