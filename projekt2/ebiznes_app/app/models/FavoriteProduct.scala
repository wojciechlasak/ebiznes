package models

import play.api.libs.json.Json

case class FavoriteProduct(id: Long, product: Long, favorite: Long)

object FavoriteProduct {
  implicit val favoriteProductFormat = Json.format[FavoriteProduct]
}