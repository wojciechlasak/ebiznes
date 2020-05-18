package controllers

import models.{Opinion, OpinionRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import javax.inject._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import play.api.libs.json.Json.toJson

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * appliprodion's home page.
 */
@Singleton
class OpinionController @Inject()(opinionRepo: OpinionRepository, productRepo: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val opinionForm: Form[CreateOpinionForm] = Form {
    mapping(
      "description" -> nonEmptyText,
      "product" -> longNumber,
    )(CreateOpinionForm.apply)(CreateOpinionForm.unapply)
  }

  val updateOpinionForm: Form[UpdateOpinionForm] = Form {
    mapping(
      "id" -> longNumber,
      "description" -> nonEmptyText,
      "product" -> longNumber,
    )(UpdateOpinionForm.apply)(UpdateOpinionForm.unapply)
  }

  def getOpinionsJSON: Action[AnyContent] = Action.async { implicit request =>
    val opinions = opinionRepo.list()
    opinions.map( opinions => Ok(toJson(opinions)))
  }

  def getOpinionJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val opinion = opinionRepo.getByIdOption(id)
    opinion.map(opinion => Ok(toJson(opinion)))
  }


  def addOpinionJSON: Action[JsValue] = Action.async(parse.json) {
    _.body.validate[Opinion] match {
      case JsSuccess(opinion, _) => opinionRepo.create(opinion.description, opinion.product).map(_ => Ok("Opinion Created!"))
      case _ => Future.successful(InternalServerError("Provided body is not valid. Please provide correct body with empty id."))
    }
  }

  def getOpinionByProductJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val opinion = opinionRepo.getByProduct(id)
    opinion.map(opinion => Ok(toJson(opinion)))
  }

  def getOpinions: Action[AnyContent] = Action.async { implicit request =>
    val opinions = opinionRepo.list()
    opinions.map( opinions => Ok(views.html.opinions(opinions)))
  }

  def getOpinion(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val opinion = opinionRepo.getByIdOption(id)
    opinion.map(opinion => opinion match {
      case Some(p) => Ok(views.html.opinion(p))
      case None => Redirect(routes.OpinionController.getOpinions())
    })
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action {
    opinionRepo.delete(id)
    Redirect("/opinions")
  }

  def updateOpinion(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    val opinion = opinionRepo.getById(id)
    opinion.map(opinion => {
      val opinForm = updateOpinionForm.fill(UpdateOpinionForm(opinion.id, opinion.description,opinion.product))
      Ok(views.html.opinionupdate(opinForm, product))
    })
  }

  def updateOpinionHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    updateOpinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.opinionupdate(errorForm, product))
        )
      },
      opinion => {
        opinionRepo.update(opinion.id, Opinion(opinion.id, opinion.description, opinion.product)).map { _ =>
          Redirect(routes.OpinionController.updateOpinion(opinion.id)).flashing("success" -> "opinion updated")
        }
      }
    )

  }


  def addOpinion: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepo.list()
    products.map (prod => Ok(views.html.opinionadd(opinionForm, prod)))
  }

  def addOpinionHandle = Action.async { implicit request =>
    var product:Seq[Product] = Seq[Product]()
    val products = productRepo.list().onComplete{
      case Success(prod) => product = prod
      case Failure(_) => print("fail")
    }

    opinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.opinionadd(errorForm, product))
        )
      },
      opinion => {
        opinionRepo.create(opinion.description, opinion.product).map { _ =>
          Redirect(routes.OpinionController.addOpinion()).flashing("success" -> "opinion.created")
        }
      }
    )

  }

}

case class CreateOpinionForm(description: String, product: Long)
case class UpdateOpinionForm(id: Long, description: String, product: Long)
