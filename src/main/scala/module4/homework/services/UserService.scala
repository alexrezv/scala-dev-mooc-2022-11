package module4.homework.services

import module4.homework.dao.entity.{Role, RoleCode, User, UserId}
import module4.homework.dao.repository.UserRepository
import module4.phoneBook.db
import zio.macros.accessible
import zio.{Has, RIO, ZIO, ZLayer}

@accessible
object UserService {
  type UserService = Has[Service]

  trait Service {
    def listUsers(): RIO[db.DataSource, List[User]]

    def listUsersDTO(): RIO[db.DataSource, List[UserDTO]]

    def addUserWithRole(user: User, roleCode: RoleCode): RIO[db.DataSource, UserDTO]

    def listUsersWithRole(roleCode: RoleCode): RIO[db.DataSource, List[UserDTO]]
  }

  class Impl(userRepo: UserRepository.Service) extends Service {
    val dc: db.Ctx.type = db.Ctx

    import dc._

    def listUsers(): RIO[db.DataSource, List[User]] =
      userRepo.list()


    def listUsersDTO(): RIO[db.DataSource, List[UserDTO]] =
      userRepo.list().flatMap(users =>
        ZIO.foreach(users) {
          user => userRepo.userRoles(UserId(user.id)).map(roles => UserDTO(user, roles.toSet))
        }
      )


    def addUserWithRole(user: User, roleCode: RoleCode): RIO[db.DataSource, UserDTO] =
      transaction(
        for {
          usr <- userRepo.createUser(user)
          _ <- userRepo.insertRoleToUser(roleCode, UserId(user.id))
          role <- userRepo.findRoleByCode(roleCode).map(_.get)
        } yield UserDTO(usr, List(role).toSet)
      )

    def listUsersWithRole(roleCode: RoleCode): RIO[db.DataSource, List[UserDTO]] =
      userRepo.findRoleByCode(roleCode)
        .someOrFail(new RuntimeException())
        .flatMap(role =>
          userRepo.listUsersWithRole(roleCode)
            .map(users => users.map(UserDTO(_, Set(role))))
        )

  }

  val live: ZLayer[UserRepository.UserRepository, Nothing, UserService] =
    ZLayer.fromService[UserRepository.Service, UserService.Service](userRepo => new Impl(userRepo))
}

case class UserDTO(user: User, roles: Set[Role])
