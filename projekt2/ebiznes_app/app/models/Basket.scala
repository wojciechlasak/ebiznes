package models

import play.api.libs.json.Json

case class Basket(id: Long, product: Long)

object Basket {
  implicit val basketFormat = Json.format[Product]
}