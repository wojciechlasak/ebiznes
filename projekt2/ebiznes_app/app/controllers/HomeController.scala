package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getProducts: Action[AnyContent] = Action {
    Ok(views.html.index("Get products"))
  }

  def getProduct(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Get product"))
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

  def getBasket(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Get basket"))
  }

  def addToBasket: Action[AnyContent] = Action {
    Ok(views.html.index("Add to basket"))
  }

  def deleteFromBasket(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Delete from basket"))
  }

  def getOpinion(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Get opinion"))
  }

  def addOpinion: Action[AnyContent] = Action {
    Ok(views.html.index("Add opinion"))
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action {
    Ok(views.html.index("Delete opinion"))
  }

}
