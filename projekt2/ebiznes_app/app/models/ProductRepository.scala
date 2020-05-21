package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
case class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  case class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def category = column[Long]("category")

    def photo = column[String]("photo")

    def categoryFk = foreignKey("cat_fk",category, cat)(_.id)

    def * = (id, name, description, category, photo) <> ((Product.apply _).tupled, Product.unapply)

  }

  import categoryRepository.CategoryTable

  private val product = TableQuery[ProductTable]

  private val cat = TableQuery[CategoryTable]

  def create(name: String, description: String, category: Long, photo: String): Future[Product] = db.run {
    (product.map(p => (p.name, p.description,p.category, p.photo))
      returning product.map(_.id)
      into {case ((name,description,category, photo),id) => Product(id,name, description,category, photo)}
      ) += (name, description, category, photo)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def getByCategory(categoryId: Long): Future[Seq[Product]] = db.run {
    product.filter(_.category === categoryId).result
  }

  def getById(id: Long): Future[Product] = db.run {
    product.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def getByCategories(categoryIds: List[Long]): Future[Seq[Product]] = db.run {
    product.filter(_.category inSet categoryIds).result
  }

  def delete(id: Long): Future[Unit] = db.run(product.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newProduct: Product): Future[Unit] = {
    val productToUpdate: Product = newProduct.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }

}
