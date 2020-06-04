package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[String]("id", O.PrimaryKey)
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String]("email")
    def roleId = column[Int]("roleId")
    def avatarUrl = column[String]("avatar_url")
    def * = (id, firstName, lastName, email, roleId, avatarUrl) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def list(): Future[Seq[User]] = db.run {
    user.result
  }

  def getById(id: String): Future[User] = db.run {
    user.filter(_.id === id).result.head
  }

  def getByIdOption(id: String): Future[Option[User]] = db.run {
    user.filter(_.id === id).result.headOption
  }
}