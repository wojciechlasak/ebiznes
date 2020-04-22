package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FavoriteProductsRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository, favoriteRepository: FavoriteRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class FavoriteProductsTable(tag: Tag) extends Table[FavoriteProducts](tag, "favorite_products") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product")
    def favorite = column[Long]("favorite")
    def product_fk = foreignKey("prod_fk",product, prod)(_.id)
    def favorite_fk = foreignKey("fav_fk",favorite, fav)(_.id)
    def * = (id, product, favorite) <> ((FavoriteProducts.apply _).tupled, FavoriteProducts.unapply)
  }

  import productRepository.ProductTable

  import favoriteRepository.FavoriteTable

  private val favoriteProducts = TableQuery[FavoriteProductsTable]

  private val prod = TableQuery[ProductTable]

  private val fav = TableQuery[FavoriteTable]


  def create(product: Long, favorite: Long): Future[FavoriteProducts] = db.run {
    (favoriteProducts.map(f => (f.product, f.favorite))
      returning favoriteProducts.map(_.id)
      into {case ((product,favorite),id) => FavoriteProducts(id,product,favorite)}
      ) += (product, favorite)
  }

  def list(): Future[Seq[FavoriteProducts]] = db.run {
    favoriteProducts.result
  }

  def getByFavorite(favorite_id: Long): Future[Seq[FavoriteProducts]] = db.run {
    favoriteProducts.filter(_.favorite === favorite_id).result
  }

  def getById(id: Long): Future[FavoriteProducts] = db.run {
    favoriteProducts.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[FavoriteProducts]] = db.run {
    favoriteProducts.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(favoriteProducts.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_favoriteProducts: FavoriteProducts): Future[Unit] = {
    val favoriteProductsToUpdate: FavoriteProducts = new_favoriteProducts.copy(id)
    db.run(favoriteProducts.filter(_.id === id).update(favoriteProductsToUpdate)).map(_ => ())
  }

}