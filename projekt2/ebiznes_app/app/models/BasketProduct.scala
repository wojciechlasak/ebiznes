package models

import play.api.libs.json.Json

case class BasketProduct(id: Long, quantity: Int, product: Long, basket: Long)

object BasketProduct {
  implicit val basketProductFormat = Json.format[BasketProduct]
}