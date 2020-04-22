package models

import play.api.libs.json.Json

case class Payment(id: Long, name: String)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}