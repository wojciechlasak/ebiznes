package controllers

import models.{FavoriteProduct, FavoriteProductRepository, Product, ProductRepository,Favorite ,FavoriteRepository}

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
class FavoriteProductController @Inject()(favoriteproductRepo: FavoriteProductRepository, productRepo: ProductRepository, favoriteRepo: FavoriteRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val favoriteproductForm: Form[CreateFavoriteProductForm] = Form {
    mapping(
      "favorite" -> longNumber,
      "product" -> longNumber,
    )(CreateFavoriteProductForm.apply)(CreateFavoriteProductForm.unapply)
  }

  val updateFavoriteProductForm: Form[UpdateFavoriteProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "favorite" -> longNumber,
      "product" -> longNumber,
    )(UpdateFavoriteProductForm.apply)(UpdateFavoriteProductForm.unapply)
  }

  def getFavoriteProducts: Action[AnyContent] = Action.async { implicit request =>
    val favoriteproducts = favoriteproductRepo.list()
    favoriteproducts.map( favoriteproducts => Ok(views.html.favoriteproducts(favoriteproducts)))
  }

  def getFavoriteProduct(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val favoriteproduct = favoriteproductRepo.getByIdOption(id)
    favoriteproduct.map(favoriteproduct => favoriteproduct match {
      case Some(p) => Ok(views.html.favoriteproduct(p))
      case None => Redirect(routes.FavoriteProductController.getFavoriteProducts())
    })
  }

  def deleteFavoriteProduct(id: Long): Action[AnyContent] = Action {
    favoriteproductRepo.delete(id)
    Redirect("/favoriteproducts")
  }

  def updateFavoriteProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var favorite:Seq[Favorite] = Seq[Favorite]()
    val favorites = favoriteRepo.list().onComplete{
      case Success(fav) => favorite = fav
      case Failure(_) => print("fail")
    }

    val favoriteproduct = favoriteproductRepo.getById(id)
    favoriteproduct.map(favoriteproduct => {
      val favprodForm = updateFavoriteProductForm.fill(UpdateFavoriteProductForm(favoriteproduct.id, favoriteproduct.product,favoriteproduct.favorite))
      Ok(views.html.favoriteproductupdate(favprodForm, product, favorite))
    })
  }

  def updateFavoriteProductHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var favorite:Seq[Favorite] = Seq[Favorite]()
    val favorites = favoriteRepo.list().onComplete{
      case Success(fav) => favorite = fav
      case Failure(_) => print("fail")
    }

    updateFavoriteProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteproductupdate(errorForm, product, favorite))
        )
      },
      favoriteproduct => {
        favoriteproductRepo.update(favoriteproduct.id, FavoriteProduct(favoriteproduct.id, favoriteproduct.product, favoriteproduct.favorite)).map { _ =>
          Redirect(routes.FavoriteProductController.updateFavoriteProduct(favoriteproduct.id)).flashing("success" -> "favoriteproduct updated")
        }
      }
    )

  }


  def addFavoriteProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val favoriteproducts = favoriteproductRepo.list()

    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var favorite:Seq[Favorite] = Seq[Favorite]()
    val favorites = favoriteRepo.list().onComplete{
      case Success(fav) => favorite = fav
      case Failure(_) => print("fail")
    }

    favoriteproducts.map ( _ => Ok(views.html.favoriteproductadd(favoriteproductForm, product, favorite)))
  }

  def addFavoriteProductHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    var favorite:Seq[Favorite] = Seq[Favorite]()
    val favorites = favoriteRepo.list().onComplete{
      case Success(fav) => favorite = fav
      case Failure(_) => print("fail")
    }

    favoriteproductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteproductadd(errorForm, product, favorite))
        )
      },
      favoriteproduct => {
        favoriteproductRepo.create(favoriteproduct.product, favoriteproduct.favorite).map { _ =>
          Redirect(routes.FavoriteProductController.addFavoriteProduct()).flashing("success" -> "favoriteproduct.created")
        }
      }
    )

  }

}

case class CreateFavoriteProductForm(product: Long, favorite: Long)
case class UpdateFavoriteProductForm(id: Long, product: Long, favorite: Long)
