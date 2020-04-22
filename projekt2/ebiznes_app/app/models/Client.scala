package models

import play.api.libs.json.Json

case class Client(id: Long, name: String, address: String)

object Client {
  implicit val clientFormat = Json.format[Client]
}