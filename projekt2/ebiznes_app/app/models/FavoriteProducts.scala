package models

import play.api.libs.json.Json

case class FavoriteProducts(id: Long, product: Long, favorite: Long)

object FavoriteProducts {
  implicit val favoriteProductsFormat = Json.format[FavoriteProducts]
}