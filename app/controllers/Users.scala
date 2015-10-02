package controllers

import dao.UsersDAO
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import models.User
import scala.concurrent.Future

/**
 * Created by frye on 9/14/15.
 */
object Users extends Controller {

  def find(id: Int) = Action.async { request =>
    request.session.get("authenticated").map { a =>
      UsersDAO.findById(id).map(user => Ok(Json.toJson(Seq(user))))
    }.getOrElse {
      Future.successful(Unauthorized("error"))
    }
  }

  def all(): Action[AnyContent] = Action.async { implicit request =>
    UsersDAO.find().map(user => Ok(Json.toJson(user)))
  }

//  def create() = Action(BodyParsers.parse.json) { request =>
//    request.session.get("authenticated").map { user =>
//      val user = request.body.validate[User#User]
//      user.fold(
//        errors => {
//          BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
//        },
//        `
//
//      )
//    }.getOrElse {
//      Unauthorized("No user found -> user not authenticated.")
//    }
//  }

}
