package dao

// Model

import java.sql.Timestamp

import models.{UserProfile, WorkOrder, User}

// Play API

import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

// Scala

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Java
import java.util._

/**
 * Created by frye on 9/17/15.
 */
object WorkOrdersDAO extends WorkOrder with User with UserProfile {
  val profile = slick.driver.MySQLDriver

  import profile.api._

  def getWorkOrderJoin = {
    val join = for {
      (workorder, userProfile) <- WorkOrders joinLeft UserProfiles on (_.inspectorId === _.userId)
    } yield (workorder, userProfile.map(w => (w.firstName, w.lastName)))
    join
  }

  /**
   * Find all work orders
   * @param start
   * @param limit
   * @return Future[Seq[(WorkOrder, Option[(Option[String], Option[String])])]]
   */
  def find(start: Int, limit: Int): Future[Seq[(WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])]] = {
    val q2 = getWorkOrderJoin.sortBy(_._1.id.desc).drop(start).take(limit)
    try db.run(q2.result)
    finally db.close
  }

  /**
   *  1 = year, 2 = month, 3 = week, 4 = day
   * @param interval
   *  limit is determining factor once interval is found out
   * @param limit
   * @param start
   * @param amount
   */
  def findByDate(status: Int, limit: Int, start: Int, amount: Int):
    Future[Seq[(WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])]] = {
    val calendar = Calendar.getInstance()
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val year: java.util.Date = format.parse(calendar.get(Calendar.YEAR) + "-01-01")
    val DAY_IN_MS: Long = 1000 * 60 * 60 * 24
    var query = getWorkOrderJoin

    status match {
      case 1 =>
        if (limit == 1) {
          query = getWorkOrderJoin.filter(item =>
            item._1.dateOfInspection >= new java.sql.Date(year.getTime()))
            .sortBy(_._1.id.desc)
            .drop(start)
            .take(amount)

        } else if (limit == -1) {
          query = getWorkOrderJoin.filter(item =>
            item._1.dateOfInspection <= new java.sql.Date(year.getTime()))
        }

    }

    try db.run(query.result)
    finally db.close

//    // First day of week
//    calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek())
//    val firstDayOfWeek: java.sql.Date = new java.sql.Date(calendar.getTime().getTime())
//
//    // Last day of week
//    calendar.set(Calendar.DAY_OF_WEEK, 7)
//    val lastDayOfWeek: java.sql.Date = new java.sql.Date(calendar.getTime().getTime())
//
//    val query = getWorkOrderJoin.filter(item =>
//      item._1.dateOfInspection >= firstDayOfWeek && item._1.dateOfInspection <= lastDayOfWeek)
//
//    try db.run(query.result)
//    finally db.close
  }

  /**
   * Find a single work order by id
   * @param id
   * @return
   */
  def findById(id: Long): Future[Option[(WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])]] = {
    val query = getWorkOrderJoin.filter(_._1.id === id)
    try db.run(query.result.headOption)
    finally db.close
  }

  /**
   * Create a new work order.
   * @param w
   */
  def create(w: WorkOrder): Unit = {
    val workOrderId = (WorkOrders returning WorkOrders.map(_.id)) += WorkOrder(w.id, w.workOrderType, w.userId, w.policyNumber, w.user, w.details, w.inspection, w.comments, w.commenterId, w.latitude, w.longtitude, w.pdfloc)
    db.run(workOrderId)
  }

  /**
   * User writes for UsersDAO.User
   */
  implicit val userWrites: Writes[dao.WorkOrdersDAO.User] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "email").write[String] and
      (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "logins").write[Int] and
      (JsPath \ "lastLogin").writeNullable[Int] and
      (JsPath \ "createdOnUtc").writeNullable[Timestamp] and
      (JsPath \ "status").writeNullable[Boolean]
    )(unlift(WorkOrdersDAO.User.unapply))

  /**
   * User reads for UsersDAO.User
   */
  implicit val userReads: Reads[dao.WorkOrdersDAO.User] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "email").read[String] and
      (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "logins").read[Int] and
      (JsPath \ "lastLogin").readNullable[Int] and
      (JsPath \ "createdOnUtc").readNullable[Timestamp] and
      (JsPath \ "status").readNullable[Boolean]
    )(dao.WorkOrdersDAO.User.apply _)

  /**
   * Format combinator w/ reads and writes for a user
   */
  implicit val userFormat: Format[dao.WorkOrdersDAO.User] =
    Format(userReads, userWrites)

  // JSON
  /**
   * Read/Writes format for java.sql.timestamp
   */
  implicit val rds: Reads[Timestamp] = (__ \ "time").read[Long].map { long => new Timestamp(long) }
  implicit val wrs: Writes[Timestamp] = (__ \ "time").write[Long].contramap { (a: Timestamp) => a.getTime }

  implicit val rd: Reads[java.sql.Time] = (__ \ "_").read[Long].map { long => new java.sql.Time(long) }
  implicit val wr: Writes[java.sql.Time] = (__ \ "_").write[Long].contramap { (a: java.sql.Time) => a.getTime }

  implicit val WorkOrderClientReads: Reads[WorkOrdersDAO.WorkOrderClient] = (
    (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "streetAddress").readNullable[String] and
      (JsPath \ "city").readNullable[String] and
      (JsPath \ "state").readNullable[String] and
      (JsPath \ "zip").readNullable[String] and
      (JsPath \ "phone").readNullable[String] and
      (JsPath \ "phone2").readNullable[String]
    )(WorkOrdersDAO.WorkOrderClient.apply _)

  implicit val WorkOrderDetailsReads: Reads[WorkOrdersDAO.WorkOrderDetails] = (
    (JsPath \ "createdOnUtc").readNullable[java.sql.Timestamp] and
      (JsPath \ "isExpert").readNullable[Boolean] and
      (JsPath \ "damageType").readNullable[String] and
      (JsPath \ "dateOfLoss").readNullable[java.sql.Date] and
      (JsPath \ "timeOfLoss").readNullable[java.sql.Time] and
      (JsPath \ "interiorInspection").readNullable[Boolean] and
      (JsPath \ "adjusterPresent").readNullable[Boolean] and
      (JsPath \ "tarped").readNullable[Boolean] and
      (JsPath \ "estimateScopeRequirement").readNullable[String] and
      (JsPath \ "specialInstructions").readNullable[String] and
      (JsPath \ "status").read[Int]
    )(WorkOrdersDAO.WorkOrderDetails.apply _)

  implicit val WorkOrderInspectionReads: Reads[WorkOrdersDAO.WorkOrderInspection] = (
    (JsPath \ "inspectorId").readNullable[Int] and
      (JsPath \ "inspectionStatus").read[Int] and
      (JsPath \ "inspectionType").readNullable[Int] and
      (JsPath \ "dateOfInspection").readNullable[java.sql.Date] and
      (JsPath \ "timeOfInspection").readNullable[java.sql.Time] and
      (JsPath \ "price").readNullable[Float] and
      (JsPath \ "isGeneratedPdf").readNullable[Boolean] and
      (JsPath \ "lastGenerated").readNullable[java.sql.Timestamp] and
      (JsPath \ "generateReportStatus").readNullable[String]
    )(WorkOrdersDAO.WorkOrderInspection.apply _)

  implicit val WorkOrderReads: Reads[WorkOrdersDAO.WorkOrder] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "workOrderType").readNullable[Int] and
      (JsPath \ "userId").readNullable[Int] and
      (JsPath \ "policyNumber").readNullable[String] and
      (JsPath \ "user").read[WorkOrdersDAO.WorkOrderClient] and
      (JsPath \ "details").read[WorkOrdersDAO.WorkOrderDetails] and
      (JsPath \ "inspection").read[WorkOrdersDAO.WorkOrderInspection] and
      (JsPath \ "comments").readNullable[String] and
      (JsPath \ "commenterId").readNullable[Int] and
      (JsPath \ "latitude").readNullable[Double] and
      (JsPath \ "longtitude").readNullable[Double] and
      (JsPath \ "pdfloc").readNullable[String]
    )(WorkOrdersDAO.WorkOrder.apply _)


  implicit val WorkOrderClientWrites: Writes[WorkOrdersDAO.WorkOrderClient] = (
    (JsPath \ "firstName").writeNullable[String] and
      (JsPath \ "lastName").writeNullable[String] and
      (JsPath \ "streetAddress").writeNullable[String] and
      (JsPath \ "city").writeNullable[String] and
      (JsPath \ "state").writeNullable[String] and
      (JsPath \ "zip").writeNullable[String] and
      (JsPath \ "phone").writeNullable[String] and
      (JsPath \ "phone2").writeNullable[String]
    )(unlift(WorkOrdersDAO.WorkOrderClient.unapply))

  implicit val WorkOrderDetailsWrites: Writes[WorkOrdersDAO.WorkOrderDetails] = (
    (JsPath \ "createdOnUtc").writeNullable[java.sql.Timestamp] and
      (JsPath \ "isExpert").writeNullable[Boolean] and
      (JsPath \ "damageType").writeNullable[String] and
      (JsPath \ "dateOfLoss").writeNullable[java.sql.Date] and
      (JsPath \ "timeOfLoss").writeNullable[java.sql.Time] and
      (JsPath \ "interiorInspection").writeNullable[Boolean] and
      (JsPath \ "adjusterPresent").writeNullable[Boolean] and
      (JsPath \ "tarped").writeNullable[Boolean] and
      (JsPath \ "estimateScopeRequirement").writeNullable[String] and
      (JsPath \ "specialInstructions").writeNullable[String] and
      (JsPath \ "status").write[Int]
    )(unlift(WorkOrdersDAO.WorkOrderDetails.unapply))

  implicit val WorkOrderInspectionWrites: Writes[WorkOrdersDAO.WorkOrderInspection] = (
    (JsPath \ "inspectorId").writeNullable[Int] and
      (JsPath \ "inspectionStatus").write[Int] and
      (JsPath \ "inspectionType").writeNullable[Int] and
      (JsPath \ "dateOfInspection").writeNullable[java.sql.Date] and
      (JsPath \ "timeOfInspection").writeNullable[java.sql.Time] and
      (JsPath \ "price").writeNullable[Float] and
      (JsPath \ "isGeneratedPdf").writeNullable[Boolean] and
      (JsPath \ "lastGenerated").writeNullable[java.sql.Timestamp] and
      (JsPath \ "generateReportStatus").writeNullable[String]
    )(unlift(WorkOrdersDAO.WorkOrderInspection.unapply))

  implicit val WorkOrderWrites: Writes[WorkOrdersDAO.WorkOrder] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "workOrderType").writeNullable[Int] and
      (JsPath \ "userId").writeNullable[Int] and
      (JsPath \ "policyNumber").writeNullable[String] and
      (JsPath \ "user").write[WorkOrdersDAO.WorkOrderClient] and
      (JsPath \ "details").write[WorkOrdersDAO.WorkOrderDetails] and
      (JsPath \ "inspection").write[WorkOrdersDAO.WorkOrderInspection] and
      (JsPath \ "comments").writeNullable[String] and
      (JsPath \ "commenterId").writeNullable[Int] and
      (JsPath \ "latitude").writeNullable[Double] and
      (JsPath \ "longtitude").writeNullable[Double] and
      (JsPath \ "pdfloc").writeNullable[String]
    )(unlift(WorkOrdersDAO.WorkOrder.unapply))

  implicit val WorkOrderUserProfileWrites: Writes[WorkOrdersDAO.UserProfile] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "firstName").writeNullable[String] and
      (JsPath \ "lastName").writeNullable[String] and
      (JsPath \ "phone").writeNullable[String] and
      (JsPath \ "geographicRegion").writeNullable[String] and
      (JsPath \ "insuranceCompany").writeNullable[String] and
      (JsPath \ "color").writeNullable[String]
    )(unlift(WorkOrdersDAO.UserProfile.unapply))

  implicit lazy val WorkUserProfileWrites: Writes[(WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])] =
    new Writes[(WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])] {
      def writes(order: (WorkOrdersDAO.WorkOrder, Option[(Option[String], Option[String])])) = Json.obj(
        "workorder" -> order._1,
        "inspector" -> Json.obj(
          "firstName" -> order._2.map(firstName => firstName._1),
          "lastName" -> order._2.map(lastName => lastName._2)
        )
      )
    }


}
