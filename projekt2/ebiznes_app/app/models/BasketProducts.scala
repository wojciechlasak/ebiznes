package models

import play.api.libs.json.Json

case class BasketProducts(id: Long, product: Long, basket: Long)

object BasketProducts {
  implicit val basketProductsFormat = Json.format[BasketProducts]
}