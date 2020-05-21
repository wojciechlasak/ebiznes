package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository, basketRepository: BasketRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class BasketProductTable(tag: Tag) extends Table[BasketProduct](tag, "basket_products") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def quantity = column[Int]("quantity")
    def product = column[Long]("product")
    def basket = column[Long]("basket")
    def productFk = foreignKey("prod_fk",product, prod)(_.id)
    def basketFk = foreignKey("bask_fk",basket, bask)(_.id)
    def * = (id, quantity, product, basket) <> ((BasketProduct.apply _).tupled, BasketProduct.unapply)
  }

  import productRepository.ProductTable

  import basketRepository.BasketTable

  private val basketProduct = TableQuery[BasketProductTable]

  private val prod = TableQuery[ProductTable]

  private val bask = TableQuery[BasketTable]


  def create(quantity: Int, product: Long, basket: Long): Future[BasketProduct] = db.run {
    (basketProduct.map(b => (b.quantity, b.product, b.basket))
      returning basketProduct.map(_.id)
      into {case ((quantity,product,basket),id) => BasketProduct(id,quantity, product,basket)}
      ) += (quantity, product, basket)
  }

  def list(): Future[Seq[BasketProduct]] = db.run {
    basketProduct.result
  }

  def getByBasket(basketId: Long): Future[Seq[BasketProduct]] = db.run {
    basketProduct.filter(_.basket === basketId).result
  }

  def getById(id: Long): Future[BasketProduct] = db.run {
    basketProduct.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[BasketProduct]] = db.run {
    basketProduct.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(basketProduct.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newBasketProduct: BasketProduct): Future[Unit] = {
    val basketProductToUpdate: BasketProduct = newBasketProduct.copy(id)
    db.run(basketProduct.filter(_.id === id).update(basketProductToUpdate)).map(_ => ())
  }

}