package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class OpinionRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class OpinionTable(tag: Tag) extends Table[Opinion](tag, "opinion") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def description = column[String]("description")

    def product = column[Long]("product")

    def product_fk = foreignKey("prod_fk",product, prod)(_.id)



    def * = (id, description, product) <> ((Opinion.apply _).tupled, Opinion.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import productRepository.ProductTable

  private val opinion = TableQuery[OpinionTable]

  private val prod = TableQuery[ProductTable]

  def create(description: String, product: Long): Future[Opinion] = db.run {
    (opinion.map(o => (o.description,o.product))
      returning opinion.map(_.id)
      into {case ((description,product),id) => Opinion(id,description,product)}
      ) += (description, product)
  }

  def list(): Future[Seq[Opinion]] = db.run {
    opinion.result
  }

  def getByProduct(product_id: Long): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.product === product_id).result
  }

  def getById(id: Long): Future[Opinion] = db.run {
    opinion.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Opinion]] = db.run {
    opinion.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(opinion.filter(_.id === id).delete).map(_ => ())

}