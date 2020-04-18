package controllers

import models.{Opinion, OpinionRepository, Product, ProductRepository, BasketProducts, BasketProductsRepository}

import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(productRepo: ProductRepository, opinionRepo: OpinionRepository, basketProductsRepo: BasketProductsRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val products = productRepo.list()
    products.map( products => Ok(views.html.products(products)))
  }

  def getProduct(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val product = productRepo.getByIdOption(id)
    product.map(product => product match {
      case Some(p) => Ok(views.html.product(p))
      case None => Redirect(routes.HomeController.getProducts())
    })
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Delete product"))
  }

  def addProduct: Action[AnyContent] = Action {
    Ok(views.html.index("Add product"))
  }

  def updateProduct: Action[AnyContent] = Action {
    Ok(views.html.index("Update product"))
  }

  def getBasket(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basketProducts = basketProductsRepo.getByIdOption(id)
    basketProducts.map(basketProducts => basketProducts match {
      case Some(b) => Ok(views.html.basketProducts(b))
    })
  }

  def addToBasket: Action[AnyContent] = Action {
    Ok(views.html.index("Add to basket"))
  }

  def deleteFromBasket(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Delete from basket"))
  }


  def getOpinion(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val opinion = opinionRepo.getByIdOption(id)
    opinion.map(opinion => opinion match {
      case Some(o) => Ok(views.html.opinion(o))
    })
  }

  def addOpinion: Action[AnyContent] = Action {
    Ok(views.html.index("Add opinion"))
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Delete opinion"))
  }

}
