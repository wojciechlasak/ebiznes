package models

import play.api.libs.json.Json

case class Favorite(id: Long, client: Long)

object Favorite {
  implicit val favoriteFormat = Json.format[Favorite]
}