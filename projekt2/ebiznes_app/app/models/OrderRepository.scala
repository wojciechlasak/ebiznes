package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class OrderRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, paymentRepository: PaymentRepository, basketRepository: BasketRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def basket = column[Long]("basket")
    def payment = column[Long]("payment")
    def basketFk = foreignKey("bask_fk",basket, bask)(_.id)
    def paymentFk = foreignKey("pay_fk",payment, pay)(_.id)
    def * = (id, basket, payment) <> ((Order.apply _).tupled, Order.unapply)
  }

  import basketRepository.BasketTable
  import paymentRepository.PaymentTable

  private val order = TableQuery[OrderTable]

  private val pay = TableQuery[PaymentTable]

  private val bask = TableQuery[BasketTable]

  def create(basket: Long, payment: Long): Future[Order] = db.run {
    (order.map(o => (o.basket, o.payment))
      returning order.map(_.id)
      into {case ((basket, payment),id) => Order(id, basket, payment)}
      ) += (basket, payment)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }

  def getByPayment(paymentId: Long): Future[Seq[Order]] = db.run {
    order.filter(_.payment === paymentId).result
  }

  def getByBasket(basketId: Long): Future[Seq[Order]] = db.run {
    order.filter(_.basket === basketId).result
  }

  def getById(id: Long): Future[Order] = db.run {
    order.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(order.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newOrder: Order): Future[Unit] = {
    val orderToUpdate: Order = newOrder.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => ())
  }

}
