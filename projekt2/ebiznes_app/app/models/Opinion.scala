package models

import play.api.libs.json.Json

case class Opinion(id: Long, description: String, product: Long)

object Opinion {
  implicit val opinionFormat = Json.format[Opinion]
}