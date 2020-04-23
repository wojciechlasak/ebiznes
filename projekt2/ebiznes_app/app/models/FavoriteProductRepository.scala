package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FavoriteProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository, favoriteRepository: FavoriteRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class FavoriteProductTable(tag: Tag) extends Table[FavoriteProduct](tag, "favorite_products") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product")
    def favorite = column[Long]("favorite")
    def product_fk = foreignKey("prod_fk",product, prod)(_.id)
    def favorite_fk = foreignKey("fav_fk",favorite, fav)(_.id)
    def * = (id, product, favorite) <> ((FavoriteProduct.apply _).tupled, FavoriteProduct.unapply)
  }

  import productRepository.ProductTable

  import favoriteRepository.FavoriteTable

  private val favoriteProduct = TableQuery[FavoriteProductTable]

  private val prod = TableQuery[ProductTable]

  private val fav = TableQuery[FavoriteTable]


  def create(product: Long, favorite: Long): Future[FavoriteProduct] = db.run {
    (favoriteProduct.map(f => (f.product, f.favorite))
      returning favoriteProduct.map(_.id)
      into {case ((product,favorite),id) => FavoriteProduct(id,product,favorite)}
      ) += (product, favorite)
  }

  def list(): Future[Seq[FavoriteProduct]] = db.run {
    favoriteProduct.result
  }

  def getByFavorite(favorite_id: Long): Future[Seq[FavoriteProduct]] = db.run {
    favoriteProduct.filter(_.favorite === favorite_id).result
  }

  def getById(id: Long): Future[FavoriteProduct] = db.run {
    favoriteProduct.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[FavoriteProduct]] = db.run {
    favoriteProduct.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(favoriteProduct.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_favoriteProduct: FavoriteProduct): Future[Unit] = {
    val favoriteProductToUpdate: FavoriteProduct = new_favoriteProduct.copy(id)
    db.run(favoriteProduct.filter(_.id === id).update(favoriteProductToUpdate)).map(_ => ())
  }

}