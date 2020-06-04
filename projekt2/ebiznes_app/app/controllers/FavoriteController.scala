package controllers

import models.{Favorite, FavoriteRepository, User, UserRepository}

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
class FavoriteController @Inject()(favoriteRepo: FavoriteRepository, userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val favoriteForm: Form[CreateFavoriteForm] = Form {
    mapping(
      "user" -> nonEmptyText,
    )(CreateFavoriteForm.apply)(CreateFavoriteForm.unapply)
  }

  val updateFavoriteForm: Form[UpdateFavoriteForm] = Form {
    mapping(
      "id" -> longNumber,
      "user" -> nonEmptyText,
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

  def getFavoriteByUserJSON(id: String): Action[AnyContent] = Action.async { implicit request =>
    val favorite = favoriteRepo.getByUser(id)
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
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(use) => user = use
      case Failure(_) => print("fail")
    }

    val favorite = favoriteRepo.getById(id)
    favorite.map(favorite => {
      val favForm = updateFavoriteForm.fill(UpdateFavoriteForm(favorite.id, favorite.user))
      Ok(views.html.favoriteupdate(favForm, user))
    })
  }

  def updateFavoriteHandle = Action.async { implicit request =>
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(use) => user = use
      case Failure(_) => print("fail")
    }

    updateFavoriteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteupdate(errorForm, user))
        )
      },
      favorite => {
        favoriteRepo.update(favorite.id, Favorite(favorite.id, favorite.user)).map { _ =>
          Redirect(routes.FavoriteController.updateFavorite(favorite.id)).flashing("success" -> "Favorite updated")
        }
      }
    )

  }


  def addFavorite: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepo.list()
    users.map (use => Ok(views.html.favoriteadd(favoriteForm, use)))
  }

  def addFavoriteHandle = Action.async { implicit request =>
    var user:Seq[User] = Seq[User]()
    userRepo.list().onComplete{
      case Success(use) => user = use
      case Failure(_) => print("fail")
    }

    favoriteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteadd(errorForm, user))
        )
      },
      favorite => {
        favoriteRepo.create(favorite.user).map { _ =>
          Redirect(routes.FavoriteController.addFavorite()).flashing("success" -> "Favorite.created")
        }
      }
    )

  }

}

case class CreateFavoriteForm(user: String)
case class UpdateFavoriteForm(id: Long, user: String)
