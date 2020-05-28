package controllers

import models.{Basket, BasketRepository, Client, ClientRepository}
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
class BasketController @Inject()(basketRepo: BasketRepository, clientRepo: ClientRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "client" -> nonEmptyText,
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  val updateBasketForm: Form[UpdateBasketForm] = Form {
    mapping(
      "id" -> longNumber,
      "client" -> nonEmptyText,
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

  def getBasketByClientJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val basket = basketRepo.getByClient(id)
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
    var client:Seq[Client] = Seq[Client]()
    clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    val basket = basketRepo.getById(id)
    basket.map(basket => {
      val baskForm = updateBasketForm.fill(UpdateBasketForm(basket.id, basket.client))
      Ok(views.html.basketupdate(baskForm, client))
    })
  }

  def updateBasketHandle = Action.async { implicit request =>
    var client:Seq[Client] = Seq[Client]()
    clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    updateBasketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketupdate(errorForm, client))
        )
      },
      basket => {
        basketRepo.update(basket.id, Basket(basket.id, basket.client)).map { _ =>
          Redirect(routes.BasketController.updateBasket(basket.id)).flashing("success" -> "Basket updated")
        }
      }
    )

  }


  def addBasket: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val clients = clientRepo.list()
    clients.map (cli => Ok(views.html.basketadd(basketForm, cli)))
  }

  def addBasketHandle = Action.async { implicit request =>
    var client:Seq[Client] = Seq[Client]()
    clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    basketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketadd(errorForm, client))
        )
      },
      basket => {
        basketRepo.create(basket.client).map { _ =>
          Redirect(routes.BasketController.addBasket()).flashing("success" -> "Basket.created")
        }
      }
    )

  }

}

case class CreateBasketForm(client: Long)
case class UpdateBasketForm(id: Long, client: Long)
