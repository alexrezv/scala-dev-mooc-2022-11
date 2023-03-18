package module3

import module3.userService.{User, UserID}
import zio.{Has, Task, ULayer}

package object userDAO {

  /**
   * Реализовать сервис с двумя методами
   *  1. list - список всех пользоватиелей
   *     2. findBy - поиск по User ID
   */

  type UserDAO = Has[UserDAO.Service]

  object UserDAO {

    trait Service {
      def list(): Task[List[User]]

      def findBy(id: UserID): Task[Option[User]]
    }

    val live: ULayer[UserDAO] = ???
  }


}
