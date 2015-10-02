package controllers

import play.api.mvc._
import play.api.libs.json._
import dao.WorkOrdersDAO
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by frye on 9/17/15.
 */
object Workorders extends Controller {

  /**
   *
   * @return
   */
  def all(start: Int, limit: Int): Action[AnyContent] = Action.async { implicit request =>
    WorkOrdersDAO.find(start, limit).map(w => Ok(Json.toJson(w)))
  }

  def find(id: Long): Action[AnyContent] = Action.async { implicit request =>
    WorkOrdersDAO.findById(id).map(w => Ok(Json.toJson(w)))
  }

  def create() = Action.async(parse.json) { request =>
    val workOrder = request.body.validate[WorkOrdersDAO.WorkOrder]
    workOrder.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      workorder => {
        val test = WorkOrdersDAO.create(workorder)
        Future.successful(Ok(Json.toJson(workorder)))
      }
    )
  }

  /**
   * Update a work order
   * @return
   */
  def update() = Action.async(parse.json) { request =>
    val workOrder = request.body.validate[WorkOrdersDAO.WorkOrder]
    workOrder.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))))
      },
      workorder => {
        val result = WorkOrdersDAO.update(workorder.id, workorder)
        WorkOrdersDAO.findById(workorder.id).map(w => Ok(Json.toJson(w)))
      }
    )
  }

  /**
   * Delete an id, but make sure a user is an admin to delete.
   * @param id
   * @return
   */
  def delete(id: Long) = Action { request =>
    val deletedId = WorkOrdersDAO.delete(id)
    Ok("Deleted work id" + id.toString())
  }

  def counts() = Action.async { request =>
    WorkOrdersDAO.counts().map(c => Ok(Json.toJson(c)))
  }

}
