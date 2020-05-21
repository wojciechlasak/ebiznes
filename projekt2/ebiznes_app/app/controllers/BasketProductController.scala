package controllers

import models.{BasketProduct, BasketProductRepository, Product, ProductRepository,Basket ,BasketRepository}

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
class BasketProductController @Inject()(basketproductRepo: BasketProductRepository, productRepo: ProductRepository, basketRepo: BasketRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketproductForm: Form[CreateBasketProductForm] = Form {
    mapping(
      "quantity" -> number,
      "product" -> longNumber,
      "basket" -> longNumber,
    )(CreateBasketProductForm.apply)(CreateBasketProductForm.unapply)
  }

  val updateBasketProductForm: Form[UpdateBasketProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "quantity" -> number,
      "product" -> longNumber,
      "basket" -> longNumber,
    )(UpdateBasketProductForm.apply)(UpdateBasketProductForm.unapply)
  }

  def getBasketProductsJSON: Action[AnyContent] = Action.async { implicit request =>
    val basketproducts = basketproductRepo.list()
    basketproducts.map( basketproducts => Ok(toJson(basketproducts)))
  }

  def getBasketProductJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basketproduct = basketproductRepo.getByIdOption(id)
    basketproduct.map(basketproduct => Ok(toJson(basketproduct)))
  }

  def getBasketProductByBasketJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basketproduct = basketproductRepo.getByBasket(id)
    basketproduct.map(basketproduct => Ok(toJson(basketproduct)))
  }

  def getBasketProducts: Action[AnyContent] = Action.async { implicit request =>
    val basketproducts = basketproductRepo.list()
    basketproducts.map( basketproducts => Ok(views.html.basketproducts(basketproducts)))
  }

  def getBasketProduct(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basketproduct = basketproductRepo.getByIdOption(id)
    basketproduct.map(basketproduct => basketproduct match {
      case Some(p) => Ok(views.html.basketproduct(p))
      case None => Redirect(routes.BasketProductController.getBasketProducts())
    })
  }

  def deleteBasketProduct(id: Long): Action[AnyContent] = Action {
    basketproductRepo.delete(id)
    Redirect("/basketproducts")
  }

  def updateBasketProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var product:Seq[Product] = Seq[Product]()
    productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    val baskets = basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    val basketproduct = basketproductRepo.getById(id)
    basketproduct.map(basketproduct => {
      val baskprodForm = updateBasketProductForm.fill(UpdateBasketProductForm(basketproduct.id,basketproduct.quantity, basketproduct.product,basketproduct.basket))
      Ok(views.html.basketproductupdate(baskprodForm, product, basket))
    })
  }

  def updateBasketProductHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    updateBasketProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketproductupdate(errorForm, product, basket))
        )
      },
      basketproduct => {
        basketproductRepo.update(basketproduct.id, BasketProduct(basketproduct.id,basketproduct.quantity, basketproduct.product, basketproduct.basket)).map { _ =>
          Redirect(routes.BasketProductController.updateBasketProduct(basketproduct.id)).flashing("success" -> "basketproduct updated")
        }
      }
    )

  }


  def addBasketProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val basketproducts = basketproductRepo.list()

    var product:Seq[Product] = Seq[Product]()
    productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    basketproducts.map ( _ => Ok(views.html.basketproductadd(basketproductForm, product, basket)))
  }

  def addBasketProductHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var basket:Seq[Basket] = Seq[Basket]()
    basketRepo.list().onComplete{
      case Success(bask) => basket = bask
      case Failure(_) => print("fail")
    }

    basketproductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketproductadd(errorForm, product, basket))
        )
      },
      basketproduct => {
        basketproductRepo.create(basketproduct.quantity, basketproduct.product, basketproduct.basket).map { _ =>
          Redirect(routes.BasketProductController.addBasketProduct()).flashing("success" -> "basketproduct.created")
        }
      }
    )

  }

}

case class CreateBasketProductForm(quantity: Int, product: Long, basket: Long)
case class UpdateBasketProductForm(id: Long,quantity: Int, product: Long, basket: Long)
