package controllers

import models.{Favorite, FavoriteRepository, Client, ClientRepository}

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
class FavoriteController @Inject()(favoriteRepo: FavoriteRepository, clientRepo: ClientRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val favoriteForm: Form[CreateFavoriteForm] = Form {
    mapping(
      "client" -> longNumber,
    )(CreateFavoriteForm.apply)(CreateFavoriteForm.unapply)
  }

  val updateFavoriteForm: Form[UpdateFavoriteForm] = Form {
    mapping(
      "id" -> longNumber,
      "client" -> longNumber,
    )(UpdateFavoriteForm.apply)(UpdateFavoriteForm.unapply)
  }

  def getFavoritesJSON: Action[AnyContent] = Action.async { implicit request =>
    val favorites = favoriteRepo.list()
    favorites.map( favorites => Ok(toJson(favorites)))
  }

  def getFavoriteJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val favorite = favoriteRepo.getByIdOption(id)
    favorite.map(favorite => Ok(toJson(favorite)))
  }

  def getFavoriteByClientJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val favorite = favoriteRepo.getByClient(id)
    favorite.map(favorite => Ok(toJson(favorite)))
  }

  def getFavorites: Action[AnyContent] = Action.async { implicit request =>
    val favorites = favoriteRepo.list()
    favorites.map( favorites => Ok(views.html.favorites(favorites)))
  }

  def getFavorite(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val favorite = favoriteRepo.getByIdOption(id)
    favorite.map(favorite => favorite match {
      case Some(p) => Ok(views.html.favorite(p))
      case None => Redirect(routes.FavoriteController.getFavorites())
    })
  }

  def deleteFavorite(id: Long): Action[AnyContent] = Action {
    favoriteRepo.delete(id)
    Redirect("/favorites")
  }

  def updateFavorite(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var client:Seq[Client] = Seq[Client]()
    val clients = clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    val favorite = favoriteRepo.getById(id)
    favorite.map(favorite => {
      val favForm = updateFavoriteForm.fill(UpdateFavoriteForm(favorite.id, favorite.client))
      Ok(views.html.favoriteupdate(favForm, client))
    })
  }

  def updateFavoriteHandle = Action.async { implicit request =>
    var client:Seq[Client] = Seq[Client]()
    clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    updateFavoriteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteupdate(errorForm, client))
        )
      },
      favorite => {
        favoriteRepo.update(favorite.id, Favorite(favorite.id, favorite.client)).map { _ =>
          Redirect(routes.FavoriteController.updateFavorite(favorite.id)).flashing("success" -> "Favorite updated")
        }
      }
    )

  }


  def addFavorite: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val clients = clientRepo.list()
    clients.map (cli => Ok(views.html.favoriteadd(favoriteForm, cli)))
  }

  def addFavoriteHandle = Action.async { implicit request =>
    var client:Seq[Client] = Seq[Client]()
    clientRepo.list().onComplete{
      case Success(cli) => client = cli
      case Failure(_) => print("fail")
    }

    favoriteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteadd(errorForm, client))
        )
      },
      favorite => {
        favoriteRepo.create(favorite.client).map { _ =>
          Redirect(routes.FavoriteController.addFavorite()).flashing("success" -> "Favorite.created")
        }
      }
    )

  }

}

case class CreateFavoriteForm(client: Long)
case class UpdateFavoriteForm(id: Long, client: Long)
