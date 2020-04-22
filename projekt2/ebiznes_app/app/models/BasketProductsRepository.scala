package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketProductsRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository, basketRepository: BasketRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class BasketProductsTable(tag: Tag) extends Table[BasketProducts](tag, "basket_products") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def quantity = column[Int]("quantity")
    def product = column[Long]("product")
    def basket = column[Long]("basket")
    def product_fk = foreignKey("prod_fk",product, prod)(_.id)
    def basket_fk = foreignKey("bask_fk",basket, bask)(_.id)
    def * = (id, quantity, product, basket) <> ((BasketProducts.apply _).tupled, BasketProducts.unapply)
  }

  import productRepository.ProductTable

  import basketRepository.BasketTable

  private val basketProducts = TableQuery[BasketProductsTable]

  private val prod = TableQuery[ProductTable]

  private val bask = TableQuery[BasketTable]


  def create(quantity: Int, product: Long, basket: Long): Future[BasketProducts] = db.run {
    (basketProducts.map(b => (b.quantity, b.product, b.basket))
      returning basketProducts.map(_.id)
      into {case ((quantity,product,basket),id) => BasketProducts(id,quantity, product,basket)}
      ) += (quantity, product, basket)
  }

  def list(): Future[Seq[BasketProducts]] = db.run {
    basketProducts.result
  }

  def getByBasket(basket_id: Long): Future[Seq[BasketProducts]] = db.run {
    basketProducts.filter(_.basket === basket_id).result
  }

  def getById(id: Long): Future[BasketProducts] = db.run {
    basketProducts.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[BasketProducts]] = db.run {
    basketProducts.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(basketProducts.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_basketProducts: BasketProducts): Future[Unit] = {
    val basketProductsToUpdate: BasketProducts = new_basketProducts.copy(id)
    db.run(basketProducts.filter(_.id === id).update(basketProductsToUpdate)).map(_ => ())
  }

}