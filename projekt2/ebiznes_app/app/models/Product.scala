package models

import play.api.libs.json.Json

case class Product(id: Long, name: String, description: String, category: Long, photo: String)

object Product {
  implicit val productFormat = Json.format[Product]
}