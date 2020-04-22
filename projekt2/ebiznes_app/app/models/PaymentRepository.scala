package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  val payment = TableQuery[PaymentTable]

  def create(name: String): Future[Payment] = db.run {
    (payment.map(p => (p.name))
      returning payment.map(_.id)
      into ((name, id) => Payment(id, name))
      ) += (name)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def delete(id: Long): Future[Unit] = db.run(payment.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_payment: Payment): Future[Unit] = {
    val paymentToUpdate: Payment = new_payment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate)).map(_ => ())
  }
}