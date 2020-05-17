package controllers

import models.{Payment, PaymentRepository}

import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json.toJson

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PaymentController @Inject()(paymentRepo: PaymentRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def getPaymentsJSON: Action[AnyContent] = Action.async { implicit request =>
    val payments = paymentRepo.list()
    payments.map( payments => Ok(toJson(payments)))
  }

  def getPaymentJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val payment = paymentRepo.getByIdOption(id)
    payment.map(payment => Ok(toJson(payment)))
  }

  def getPayments: Action[AnyContent] = Action.async { implicit request =>
    val payments = paymentRepo.list()
    payments.map( payments => Ok(views.html.payments(payments)))
  }

  def getPayment(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val payment = paymentRepo.getByIdOption(id)
    payment.map(payment => payment match {
      case Some(p) => Ok(views.html.payment(p))
      case None => Redirect(routes.PaymentController.getPayments())
    })
  }

  def deletePayment(id: Long): Action[AnyContent] = Action {
    paymentRepo.delete(id)
    Redirect("/payments")
  }

  def updatePayment(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payment = paymentRepo.getById(id)
    payment.map(payment => {
      val cliForm = updatePaymentForm.fill(UpdatePaymentForm(payment.id, payment.name))
      Ok(views.html.paymentupdate(cliForm))
    })
  }

  def updatePaymentHandle = Action.async { implicit request =>
    updatePaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.paymentupdate(errorForm))
        )
      },
      payment => {
        paymentRepo.update(payment.id, Payment(payment.id, payment.name)).map { _ =>
          Redirect(routes.PaymentController.updatePayment(payment.id)).flashing("success" -> "payment updated")
        }
      }
    )

  }


  def addPayment: Action[AnyContent] = Action.async { implicit request =>
    val payments = paymentRepo.list()
    payments.map (cli => Ok(views.html.paymentadd(paymentForm)))
  }

  def addPaymentHandle = Action.async { implicit request =>

    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.paymentadd(errorForm))
        )
      },
      payment => {
        paymentRepo.create(payment.name).map { _ =>
          Redirect(routes.PaymentController.addPayment()).flashing("success" -> "payment.created")
        }
      }
    )

  }

}

case class CreatePaymentForm(name: String)
case class UpdatePaymentForm(id: Long, name: String)
