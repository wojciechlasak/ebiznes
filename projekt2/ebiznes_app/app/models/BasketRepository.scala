package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BasketRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (id) <> (Basket.apply, Basket.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  private val basket = TableQuery[BasketTable]

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getById(id: Long): Future[Basket] = db.run {
    basket.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Basket]] = db.run {
    basket.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(basket.filter(_.id === id).delete).map(_ => ())

}