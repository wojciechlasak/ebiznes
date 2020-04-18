package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BasketRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def product = column[Long]("product")

    def product_fk = foreignKey("prod_fk",product, prod)(_.id)



    def * = (id, product) <> ((Basket.apply _).tupled, Basket.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import productRepository.ProductTable

  private val basket = TableQuery[BasketTable]

  private val prod = TableQuery[ProductTable]


  def create(product: Long): Future[Basket] = db.run {
    (basket.map(b => (b.product))
      returning basket.map(_.id)
      into {case ((product),id) => Basket(id,product)}
      ) += (product)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getById(id: Long): Future[Basket] = db.run {
    basket.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Unit] = db.run(basket.filter(_.id === id).delete).map(_ => ())

}