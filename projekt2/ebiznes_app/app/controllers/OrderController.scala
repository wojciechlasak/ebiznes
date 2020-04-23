package controllers

import models.{Order, OrderRepository, Payment, PaymentRepository,Basket ,BasketRepository}

import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class OrderController @Inject()(orderRepo: OrderRepository, paymentRepo: PaymentRepository, basketRepo: BasketRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "basket" -> longNumber,
      "payment" -> longNumber,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> longNumber,
      "basket" -> longNumber,
      "payment" -> longNumber,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  def getOrders: Action[AnyContent] = Action.async { implicit request =>
    val orders = orderRepo.list()
    orders.map( orders => Ok(views.html.orders(orders)))
  }

  def getOrder(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val order = orderRepo.getByIdOption(id)
    order.map(order => order match {
      case Some(p) => Ok(views.html.order(p))
      case None => Redirect(routes.OrderController.getOrders())
    })
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action {
    orderRepo.delete(id)
    Redirect("/orders")
  }

  def updateOrder(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var payment:Seq[Payment] = Seq[Payment]()
    val payments = paymentRepo.list().onComplete{
      case Success(pay) => payment = pay
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    val baskets = basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    val order = orderRepo.getById(id)
    order.map(order => {
      val ordForm = updateOrderForm.fill(UpdateOrderForm(order.id, order.payment,order.basket))
      Ok(views.html.orderupdate(ordForm, payment, basket))
    })
  }

  def updateOrderHandle = Action.async { implicit request =>
    var payment:Seq[Payment] = Seq[Payment]()
    val payments = paymentRepo.list().onComplete{
      case Success(pay) => payment = pay
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    val baskets = basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    updateOrderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderupdate(errorForm, payment, basket))
        )
      },
      order => {
        orderRepo.update(order.id, Order(order.id, order.payment, order.basket)).map { _ =>
          Redirect(routes.OrderController.updateOrder(order.id)).flashing("success" -> "order updated")
        }
      }
    )

  }


  def addOrder: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val orders = orderRepo.list()

    var payment:Seq[Payment] = Seq[Payment]()
    val payments = paymentRepo.list().onComplete{
      case Success(pay) => payment = pay
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    val baskets = basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    orders.map ( _ => Ok(views.html.orderadd(orderForm, payment, basket)))
  }

  def addOrderHandle = Action.async { implicit request =>
    var payment:Seq[Payment] = Seq[Payment]()
    val payments = paymentRepo.list().onComplete{
      case Success(pay) => payment = pay
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    val baskets = basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderadd(errorForm, payment, basket))
        )
      },
      order => {
        orderRepo.create(order.payment, order.basket).map { _ =>
          Redirect(routes.OrderController.addOrder()).flashing("success" -> "order.created")
        }
      }
    )

  }

}

case class CreateOrderForm(payment: Long, basket: Long)
case class UpdateOrderForm(id: Long, payment: Long, basket: Long)
