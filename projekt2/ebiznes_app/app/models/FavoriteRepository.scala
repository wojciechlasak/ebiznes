package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class FavoriteRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, clientRepository: ClientRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class FavoriteTable(tag: Tag) extends Table[Favorite](tag, "favorite") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def client = column[Long]("client")
    def client_fk = foreignKey("cli_fk",client, cli)(_.id)
    def * = (id, client) <> ((Favorite.apply _).tupled, Favorite.unapply)

  }

  import clientRepository.ClientTable

  private val cli = TableQuery[ClientTable]

  private val favorite = TableQuery[FavoriteTable]

  def create(client: Long): Future[Favorite] = db.run {
    (favorite.map(b => (b.client))
      returning favorite.map(_.id)
      into {case ((client),id) => Favorite(id,client)}
      ) += (client)
  }

  def list(): Future[Seq[Favorite]] = db.run {
    favorite.result
  }

  def getByClient(client_id: Long): Future[Seq[Favorite]] = db.run {
    favorite.filter(_.client === client_id).result
  }

  def getById(id: Long): Future[Favorite] = db.run {
    favorite.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Favorite]] = db.run {
    favorite.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(favorite.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_favorite: Favorite): Future[Unit] = {
    val favoriteToUpdate: Favorite = new_favorite.copy(id)
    db.run(favorite.filter(_.id === id).update(favoriteToUpdate)).map(_ => ())
  }

}