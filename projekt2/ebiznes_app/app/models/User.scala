package models

import play.api.libs.json.Json

case class User(id: String, firstName: String, lastName: String, email: String, roleId: Int, avatarUrl: String)

object User {
  implicit val userFormat = Json.format[User]
}