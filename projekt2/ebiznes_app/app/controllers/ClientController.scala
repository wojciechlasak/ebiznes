package controllers

import models.{Client, ClientRepository}

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
class ClientController @Inject()(clientRepo: ClientRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val clientForm: Form[CreateClientForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
    )(CreateClientForm.apply)(CreateClientForm.unapply)
  }

  val updateClientForm: Form[UpdateClientForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
    )(UpdateClientForm.apply)(UpdateClientForm.unapply)
  }

  def getClientsJSON: Action[AnyContent] = Action.async { implicit request =>
    val clients = clientRepo.list()
    clients.map( clients => Ok(toJson(clients)))
  }

  def getClientJSON(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val client = clientRepo.getByIdOption(id)
    client.map(client => Ok(toJson(client)))
  }

  def getClients: Action[AnyContent] = Action.async { implicit request =>
    val clients = clientRepo.list()
    clients.map( clients => Ok(views.html.clients(clients)))
  }

  def getClient(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val client = clientRepo.getByIdOption(id)
    client.map(client => client match {
      case Some(p) => Ok(views.html.client(p))
      case None => Redirect(routes.ClientController.getClients())
    })
  }

  def deleteClient(id: Long): Action[AnyContent] = Action {
    clientRepo.delete(id)
    Redirect("/clients")
  }

  def updateClient(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val client = clientRepo.getById(id)
    client.map(client => {
      val cliForm = updateClientForm.fill(UpdateClientForm(client.id, client.name, client.address))
      Ok(views.html.clientupdate(cliForm))
    })
  }

  def updateClientHandle = Action.async { implicit request =>
    updateClientForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.clientupdate(errorForm))
        )
      },
      client => {
        clientRepo.update(client.id, Client(client.id, client.name, client.address)).map { _ =>
          Redirect(routes.ClientController.updateClient(client.id)).flashing("success" -> "client updated")
        }
      }
    )

  }


  def addClient: Action[AnyContent] = Action.async { implicit request =>
    val clients = clientRepo.list()
    clients.map (_ => Ok(views.html.clientadd(clientForm)))
  }

  def addClientHandle = Action.async { implicit request =>

    clientForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.clientadd(errorForm))
        )
      },
      client => {
        clientRepo.create(client.name, client.address).map { _ =>
          Redirect(routes.ClientController.addClient()).flashing("success" -> "client.created")
        }
      }
    )

  }

}

case class CreateClientForm(name: String, address: String)
case class UpdateClientForm(id: Long, name: String, address: String)
