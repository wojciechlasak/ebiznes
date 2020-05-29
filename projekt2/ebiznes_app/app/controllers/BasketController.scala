package controllers

import models.{Basket, BasketRepository, User, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.libs.json.Json.toJson
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BasketController @Inject()(basketRepo: BasketRepository, userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "isOrdered" -> number,
      "user" -> nonEmptyText,
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  val updateBasketForm: Form[UpdateBasketForm] = Form {
    mapping(
      "id" -> longNumber,
      "isOrdered" -> number,
      "user" -> nonEmptyText,
    )(UpdateBasketForm.apply)(UpdateBasketForm.unapply)
  }

  def getBasketsJSON: Action[AnyContent] = Action.async { implicit request =>
    val baskets = basketRepo.list()
    baskets.map( baskets => Ok(toJson(baskets)))
  }

  def getBasketJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basket = basketRepo.getByIdOption(id)
    basket.map(basket => Ok(toJson(basket)))
  }

  def getBasketByUserJSON(id: String): Action[AnyContent] = Action.async { implicit request =>
    val basket = basketRepo.getByUser(id)
    basket.map(basket => Ok(toJson(basket)))
  }

  def getBaskets: Action[AnyContent] = Action.async { implicit request =>
    val baskets = basketRepo.list()
    baskets.map( baskets => Ok(views.html.baskets(baskets)))
  }

  def getBasket(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basket = basketRepo.getByIdOption(id)
    basket.map(basket => basket match {
      case Some(p) => Ok(views.html.basket(p))
      case None => Redirect(routes.BasketController.getBaskets())
    })
  }

  def deleteBasket(id: Long): Action[AnyContent] = Action {
    basketRepo.delete(id)
    Redirect("/baskets")
  }

  def updateBasket(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(cli) => user = cli
      case Failure(_) => print("fail")
    }

    val basket = basketRepo.getById(id)
    basket.map(basket => {
      val baskForm = updateBasketForm.fill(UpdateBasketForm(basket.id, basket.isOrdered, basket.user))
      Ok(views.html.basketupdate(baskForm, user))
    })
  }

  def updateBasketHandle = Action.async { implicit request =>
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(cli) => user = cli
      case Failure(_) => print("fail")
    }

    updateBasketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketupdate(errorForm, user))
        )
      },
      basket => {
        basketRepo.update(basket.id, Basket(basket.id, basket.isOrdered, basket.user)).map { _ =>
          Redirect(routes.BasketController.updateBasket(basket.id)).flashing("success" -> "Basket updated")
        }
      }
    )

  }


  def addBasket: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepo.list()
    users.map (cli => Ok(views.html.basketadd(basketForm, cli)))
  }

  def addBasketHandle = Action.async { implicit request =>
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(cli) => user = cli
      case Failure(_) => print("fail")
    }

    basketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketadd(errorForm, user))
        )
      },
      basket => {
        basketRepo.create(basket.isOrdered, basket.user).map { _ =>
          Redirect(routes.BasketController.addBasket()).flashing("success" -> "Basket.created")
        }
      }
    )

  }

}

case class CreateBasketForm(isOrdered: Int, user: String)
case class UpdateBasketForm(id: Long, isOrdered: Int, user: String)
