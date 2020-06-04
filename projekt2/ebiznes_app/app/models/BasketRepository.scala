package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BasketRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def isOrdered = column[Int] ("isOrdered")
    def user = column[String]("user")
    def * = (id, isOrdered, user) <> ((Basket.apply _).tupled, Basket.unapply)

  }


  private val basket = TableQuery[BasketTable]


  def create(isOrdered: Int, user: String): Future[Basket] = db.run {
    (basket.map(b => (b.isOrdered, b.user))
      returning basket.map(_.id)
      into {case ((isOrdered, user),id) => Basket(id,isOrdered, user)}
      ) += (isOrdered, user)
  }

  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getByUser(userId: String): Future[Seq[Basket]] = db.run {
    basket.filter(_.user === userId).result
  }

  def getById(id: Long): Future[Basket] = db.run {
    basket.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Basket]] = db.run {
    basket.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(basket.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newBasket: Basket): Future[Unit] = {
    val basketToUpdate: Basket = newBasket.copy(id)
    db.run(basket.filter(_.id === id).update(basketToUpdate)).map(_ => ())
  }

}