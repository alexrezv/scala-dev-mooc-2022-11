package module4.phoneBook.dao.repositories

import io.getquill.context.ZioJdbc._
import module4.phoneBook.dao.entities._
import module4.phoneBook.db
import zio.{Has, ULayer, ZLayer}

object PhoneRecordRepository {
  val ctx = db.Ctx
  import ctx._

  type PhoneRecordRepository = Has[Service]

  trait Service{
      def find(phone: String): QIO[Option[PhoneRecord]]
      def list(): QIO[List[PhoneRecord]]
      def insert(phoneRecord: PhoneRecord): QIO[Unit]
      def update(phoneRecord: PhoneRecord): QIO[Unit]
      def delete(id: String): QIO[Unit]
  }

  class ServiceImpl extends Service{

    val phoneRecordSchema = quote{
      querySchema[PhoneRecord](""""PhoneRecord"""")
    }

    val addressSchema = quote{
      querySchema[Address](""""Address"""")
    }

    // SELECT x1."id", x1."phone", x1."fio", x1."addressId" 
    // FROM "PhoneRecord" x1 WHERE x1."phone" = ?

    //SELECT x1."id", x1."phone", x1."fio", x1."addressId" FROM "PhoneRecord" x1 
    // WHERE x1."phone" = ? ORDER BY x1."phone" ASC NULLS FIRST LIMIT 1
    def find(phone: String): QIO[Option[PhoneRecord]] = 
      ctx.run(phoneRecordSchema.filter(_.phone == lift(phone)).sortBy(_.phone).take(1))
      .map(_.headOption)
    
    def list(): QIO[List[PhoneRecord]] = ctx.run(phoneRecordSchema)
    
    //INSERT INTO "PhoneRecord" ("id","phone","fio","addressId") 
    // VALUES (?, ?, ?, ?)bloo
    def insert(phoneRecord: PhoneRecord): QIO[Unit] = 
      ctx.run(phoneRecordSchema.insert(lift(phoneRecord))).unit
    
    // UPDATE "PhoneRecord" SET "id" = ?, "phone" = ?, "fio" = ?, "addressId" = ?
    // UPDATE "PhoneRecord" SET "id" = ?, "phone" = ?, "fio" = ?, 
    // "addressId" = ? WHERE "id" = ?

    def update(phoneRecord: PhoneRecord): QIO[Unit] = 
      ctx.run(phoneRecordSchema.filter(_.id == lift(phoneRecord.id))
      .update(lift(phoneRecord))).unit
    
    def delete(id: String): QIO[Unit] = 
      ctx.run(phoneRecordSchema.filter(_.id == lift(id)).delete).unit

    // implicit join

    // SELECT phr."id", phr."phone", phr."fio", phr."addressId", address."id", address."zipCode", 
    // address."streetAddress" FROM "PhoneRecord" phr, "Address" address 
    // WHERE address."id" = phr."addressId"

    ctx.run(
      for{
        phr <- phoneRecordSchema
        address <- addressSchema if(address.id == phr.addressId)
      } yield (phr, address)
    )

    // applicative join

    //SELECT x6."id", x6."phone", x6."fio", x6."addressId", x7."id", x7."zipCode", 
    // x7."streetAddress" FROM "PhoneRecord" x6 
    // INNER JOIN "Address" x7 ON x6."addressId" = x7."id"
    ctx.run(
      phoneRecordSchema.join(addressSchema).on(_.addressId == _.id)
    )

    // flat join

    // SELECT phr."id", phr."phone", phr."fio", phr."addressId" FROM "PhoneRecord" phr 
    // INNER JOIN "Address" x8 ON x8."id" = phr."addressId"
    ctx.run(
      for{
        phr <- phoneRecordSchema
        address <- addressSchema.join(_.id == phr.addressId)
        phr2 <- phoneRecordSchema
      } yield (phr, phr2)
    )

    // SELECT p.phone = '' FROM 
    // (SELECT x9."phone" AS phone FROM "PhoneRecord" x9 WHERE x9."id" = ?) 
    // AS p
    ctx.run(
      phoneRecordSchema.filter(_.id == lift(""))
      .nested.map(p => p.phone == "")
    )
    
  }
 
  val live: ULayer[PhoneRecordRepository] = ZLayer.succeed(new ServiceImpl)
}
