package controllers

import models.{Category, CategoryRepository}

import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json.toJson
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CategoryController @Inject()(categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def getCategoriesJSON: Action[AnyContent] = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map( categories => Ok(toJson(categories)))
  }

  def getCategoryJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val category = categoryRepo.getByIdOption(id)
    category.map(category => Ok(toJson(category)))
  }

  def getCategories: Action[AnyContent] = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map( categories => Ok(views.html.categories(categories)))
  }

  def getCategory(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val category = categoryRepo.getByIdOption(id)
    category.map(category => category match {
      case Some(p) => Ok(views.html.category(p))
      case None => Redirect(routes.CategoryController.getCategories())
    })
  }

  def deleteCategory(id: Long): Action[AnyContent] = Action {
    categoryRepo.delete(id)
    Redirect("/categories")
  }

  def updateCategory(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepo.getById(id)
    category.map(category => {
      val catForm = updateCategoryForm.fill(UpdateCategoryForm(category.id, category.name))
      Ok(views.html.categoryupdate(catForm))
    })
  }

  def updateCategoryHandle = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categoryupdate(errorForm))
        )
      },
      category => {
        categoryRepo.update(category.id, Category(category.id, category.name)).map { _ =>
          Redirect(routes.CategoryController.updateCategory(category.id)).flashing("success" -> "category updated")
        }
      }
    )

  }


  def addCategory: Action[AnyContent] = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map (cat => Ok(views.html.categoryadd(categoryForm)))
  }

  def addCategoryHandle = Action.async { implicit request =>

    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categoryadd(errorForm))
        )
      },
      category => {
        categoryRepo.create(category.name).map { _ =>
          Redirect(routes.CategoryController.addCategory()).flashing("success" -> "category.created")
        }
      }
    )

  }

}

case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(id: Long, name: String)
