package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
case class FavoriteRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  case class FavoriteTable(tag: Tag) extends Table[Favorite](tag, "favorite") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def user = column[String]("user")
    def userFk = foreignKey("use_fk",user, use)(_.id)
    def * = (id, user) <> ((Favorite.apply _).tupled, Favorite.unapply)

  }

  import userRepository.UserTable

  private val use = TableQuery[UserTable]

  private val favorite = TableQuery[FavoriteTable]

  def create(user: String): Future[Favorite] = db.run {
    (favorite.map(b => (b.user))
      returning favorite.map(_.id)
      into {case ((user),id) => Favorite(id,user)}
      ) += (user)
  }

  def list(): Future[Seq[Favorite]] = db.run {
    favorite.result
  }

  def getByUser(userId: String): Future[Seq[Favorite]] = db.run {
    favorite.filter(_.user === userId).result
  }

  def getById(id: Long): Future[Favorite] = db.run {
    favorite.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Favorite]] = db.run {
    favorite.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(favorite.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newFavorite: Favorite): Future[Unit] = {
    val favoriteToUpdate: Favorite = newFavorite.copy(id)
    db.run(favorite.filter(_.id === id).update(favoriteToUpdate)).map(_ => ())
  }

}