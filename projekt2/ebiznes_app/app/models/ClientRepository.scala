package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ClientTable(tag: Tag) extends Table[Client](tag, "client") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def address = column[String]("address")
    def * = (id, name, address) <> ((Client.apply _).tupled, Client.unapply)
  }

  val client = TableQuery[ClientTable]

  def create(name: String, address: String): Future[Client] = db.run {
    (client.map(c => (c.name, c.address))
      returning client.map(_.id)
      into {case ((name, address),id) => Client(id,name, address)}
      ) += (name, address)
  }

  def list(): Future[Seq[Client]] = db.run {
    client.result
  }

  def getById(id: Long): Future[Client] = db.run {
    client.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Client]] = db.run {
    client.filter(_.id === id).result.headOption
  }
  
  def delete(id: Long): Future[Unit] = db.run(client.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newClient: Client): Future[Unit] = {
    val clientToUpdate: Client = newClient.copy(id)
    db.run(client.filter(_.id === id).update(clientToUpdate)).map(_ => ())
  }
}