package module3.cats_effect_homework

import cats.effect.Sync
import cats.implicits._
import module3.cats_effect_homework.Wallet._

import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.{Files, Paths}

// DSL управления электронным кошельком
trait Wallet[F[_]] {
  // возвращает текущий баланс
  def balance: F[BigDecimal]

  // пополняет баланс на указанную сумму
  def topup(amount: BigDecimal): F[Unit]

  // списывает указанную сумму с баланса (ошибка если средств недостаточно)
  def withdraw(amount: BigDecimal): F[Either[WalletError, Unit]]
}

// Игрушечный кошелек который сохраняет свой баланс в файл
// todo: реализовать используя java.nio.file._
// Насчёт безопасного конкуррентного доступа и производительности не заморачиваемся, делаем максимально простую рабочую имплементацию. (Подсказка - можно читать и сохранять файл на каждую операцию).
// Важно аккуратно и правильно завернуть в IO все возможные побочные эффекты.
//
// функции которые пригодятся:
// - java.nio.file.Files.write
// - java.nio.file.Files.readString
// - java.nio.file.Files.exists
// - java.nio.file.Paths.get
final class FileWallet[F[_] : Sync](id: WalletId) extends Wallet[F] {
  def balance: F[BigDecimal] = {
    val path = Paths.get(id)
    for {
      str <- Sync[F].blocking(Files.readString(path)).ensuring(Files.exists(path))
      res <- Sync[F].delay(BigDecimal.apply(str))
    } yield res
  }

  def topup(amount: BigDecimal): F[Unit] = {
    val path = Paths.get(id)
    for {
      bal <- balance
      res = bal + amount
      _ <- Sync[F].blocking(Files.writeString(path, res.toString()))
    } yield ()
  }

  def withdraw(amount: BigDecimal): F[Either[WalletError, Unit]] = {
    val path = Paths.get(id)
    for {
      bal <- balance
      res <- bal match {
        case a if a < amount => Sync[F].delay(BalanceTooLow.asLeft)
        case b => Sync[F].blocking(Right(Files.writeString(path, (b - amount).toString())).map(_ => ()))
      }
    } yield res
  }
}

object Wallet {

  // todo: реализовать конструктор
  // внимание на сигнатуру результата - инициализация кошелька имеет сайд-эффекты
  // Здесь нужно использовать обобщенную версию уже пройденного вами метода IO.delay,
  // вызывается она так: Sync[F].delay(...)
  // Тайпкласс Sync из cats-effect описывает возможность заворачивания сайд-эффектов
  def fileWallet[F[_] : Sync](id: WalletId): F[Wallet[F]] = for {
    _ <- Sync[F].blocking(Files.writeString(Paths.get(id), "0", TRUNCATE_EXISTING))
  } yield new FileWallet[F](id)

  type WalletId = String

  sealed trait WalletError

  case object BalanceTooLow extends WalletError
}
