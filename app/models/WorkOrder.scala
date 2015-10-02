package models

import java.util.Calendar

import play.api.db.DB
import slick.driver.MySQLDriver.profile.api._
import play.api.Play.current
import slick.jdbc.{StaticQuery => Q, GetResult}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by frye on 9/17/15.
 */
trait WorkOrder {

	import slick.model.ForeignKeyAction
	import slick.collection.heterogeneous._
	import slick.collection.heterogeneous.syntax._

	// NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.

	import slick.jdbc.{GetResult => GR}
	import play.api.libs.json._
	import play.api.libs.json.Reads._

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @param streetAddress
	 * @param city
	 * @param state
	 * @param zip
	 * @param phone
	 * @param phone2
	 */
	case class WorkOrderClient(firstName: Option[String],
	                           lastName: Option[String],
	                           streetAddress: Option[String],
	                           city: Option[String],
	                           state: Option[String],
	                           zip: Option[String],
	                           phone: Option[String],
	                           phone2: Option[String])

	/**
	 *
	 * @param createdOnUtc
	 * @param isExpert
	 * @param damageType
	 * @param dateOfLoss
	 * @param timeOfLoss
	 * @param interiorInspection
	 * @param adjusterPresent
	 * @param tarped
	 * @param estimateScopeRequirement
	 * @param specialInstructions
	 * @param status
	 */
	case class WorkOrderDetails(createdOnUtc: Option[java.sql.Timestamp],
	                            isExpert: Option[Boolean],
	                            damageType: Option[String],
	                            dateOfLoss: Option[java.sql.Date],
	                            timeOfLoss: Option[java.sql.Time],
	                            interiorInspection: Option[Boolean],
	                            adjusterPresent: Option[Boolean],
	                            tarped: Option[Boolean],
	                            estimateScopeRequirement: Option[String],
	                            specialInstructions: Option[String],
	                            status: Int)

	/**
	 *
	 * @param inspectorId
	 * @param inspectionStatus
	 * @param inspectionType
	 * @param dateOfInspection
	 * @param timeOfInspection
	 * @param price
	 * @param isGeneratedPdf
	 * @param lastGenerated
	 * @param generateReportStatus
	 */
	case class WorkOrderInspection(inspectorId: Option[Int],
	                               inspectionStatus: Int,
	                               inspectionType: Option[Int],
	                               dateOfInspection: Option[java.sql.Date],
	                               timeOfInspection: Option[java.sql.Time],
	                               price: Option[Float],
	                               isGeneratedPdf: Option[Boolean],
	                               lastGenerated: Option[java.sql.Timestamp],
	                               generateReportStatus: Option[String])

	/**
	 *
	 * @param id
	 * @param type
	 * @param userId
	 * @param policyNumber
	 * @param user
	 * @param details
	 * @param inspector
	 * @param comments
	 * @param commenterId
	 * @param latitude
	 * @param longtitude
	 * @param pdfloc
	 */
	case class WorkOrder(id: Long,
	                     workOrderType: Option[Int],
	                     userId: Option[Int],
	                     policyNumber: Option[String],
	                     var user: WorkOrderClient,
	                     var details: WorkOrderDetails,
	                     var inspection: WorkOrderInspection,
	                     comments: Option[String],
	                     commenterId: Option[Int],
	                     latitude: Option[Double],
	                     longtitude: Option[Double],
	                     pdfloc: Option[String])

	class WorkOrders(_tableTag: Tag) extends Table[WorkOrder](_tableTag, "work_orders") with User {

		/** Projection **/
		def * = (
			id, workOrderType, userId, policyNumber,
			(firstName, lastName, streetAddress, city, state, zip, phone, phone2),
			(createdOnUtc, isExpert, damageType, dateOfLoss, timeOfLoss, interiorInspection, adjusterPresent, tarped, estimateScopeRequirement, specialInstructions, status),
			(inspectorId, inspectionStatus, inspectionType, dateOfInspection, timeOfInspection, price, isGeneratedPdf, lastGenerated, generateReportStatus),
			comments, commenterId, latitude, longtitude, pdfloc
			).shaped <>( {
			case (id, workOrderType, userId, policyNumber, user, details, inspection, comments, commenterId, latitude, longtitude, pdfloc) =>
				WorkOrder(id, workOrderType, userId, policyNumber, WorkOrderClient.tupled.apply(user),
					WorkOrderDetails.tupled.apply(details), WorkOrderInspection.tupled.apply(inspection),
					comments, commenterId, latitude, longtitude, pdfloc)
		}, { w: WorkOrder =>
			def f1(p: WorkOrderClient) = WorkOrderClient.unapply(p).get
			def f2(p: WorkOrderDetails) = WorkOrderDetails.unapply(p).get
			def f3(p: WorkOrderInspection) = WorkOrderInspection.unapply(p).get
			Some((w.id, w.workOrderType, w.userId, w.policyNumber, f1(w.user), f2(w.details), f3(w.inspection),
				w.comments, w.commenterId, w.latitude, w.longtitude, w.pdfloc))
		}
			)

		/** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
		val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
		/** Database column type SqlType(INT UNSIGNED), Default(None)
		  * NOTE: The name was escaped because it collided with a Scala keyword. */
		val workOrderType: Rep[Option[Int]] = column[Option[Int]]("type", O.Default(None))
		/** Database column user_id SqlType(INT UNSIGNED), Default(None) */
		val userId: Rep[Option[Int]] = column[Option[Int]]("user_id", O.Default(None))
		/** Database column policy_number SqlType(VARCHAR), Length(250,true), Default(None) */
		val policyNumber: Rep[Option[String]] = column[Option[String]]("policy_number", O.Length(250, varying = true), O.Default(None))
		/** Database column first_name SqlType(VARCHAR), Length(200,true), Default(None) */
		val firstName: Rep[Option[String]] = column[Option[String]]("first_name", O.Length(200, varying = true), O.Default(None))
		/** Database column last_name SqlType(VARCHAR), Length(200,true), Default(None) */
		val lastName: Rep[Option[String]] = column[Option[String]]("last_name", O.Length(200, varying = true), O.Default(None))
		/** Database column street_address SqlType(VARCHAR), Length(200,true), Default(None) */
		val streetAddress: Rep[Option[String]] = column[Option[String]]("street_address", O.Length(200, varying = true), O.Default(None))
		/** Database column city SqlType(VARCHAR), Length(200,true), Default(None) */
		val city: Rep[Option[String]] = column[Option[String]]("city", O.Length(200, varying = true), O.Default(None))
		/** Database column state SqlType(VARCHAR), Length(250,true), Default(None) */
		val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(250, varying = true), O.Default(None))
		/** Database column zip SqlType(VARCHAR), Length(5,true), Default(None) */
		val zip: Rep[Option[String]] = column[Option[String]]("zip", O.Length(5, varying = true), O.Default(None))
		/** Database column phone SqlType(VARCHAR), Length(30,true), Default(None) */
		val phone: Rep[Option[String]] = column[Option[String]]("phone", O.Length(30, varying = true), O.Default(None))
		/** Database column phone2 SqlType(VARCHAR), Length(30,true), Default(None) */
		val phone2: Rep[Option[String]] = column[Option[String]]("phone2", O.Length(30, varying = true), O.Default(None))
		/** Database column created_on_utc SqlType(DATETIME), Default(None) */
		val createdOnUtc: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created_on_utc", O.Default(None))
		/** Database column is_expert SqlType(BIT), Default(None) */
		val isExpert: Rep[Option[Boolean]] = column[Option[Boolean]]("is_expert", O.Default(None))
		/** Database column damage_type SqlType(VARCHAR), Length(200,true), Default(None) */
		val damageType: Rep[Option[String]] = column[Option[String]]("damage_type", O.Length(200, varying = true), O.Default(None))
		/** Database column date_of_loss SqlType(DATE), Default(None) */
		val dateOfLoss: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("date_of_loss", O.Default(None))
		/** Database column time_of_loss SqlType(TIME), Default(None) */
		val timeOfLoss: Rep[Option[java.sql.Time]] = column[Option[java.sql.Time]]("time_of_loss", O.Default(None))
		/** Database column interior_inspection SqlType(BIT), Default(None) */
		val interiorInspection: Rep[Option[Boolean]] = column[Option[Boolean]]("interior_inspection", O.Default(None))
		/** Database column adjuster_present SqlType(BIT), Default(None) */
		val adjusterPresent: Rep[Option[Boolean]] = column[Option[Boolean]]("adjuster_present", O.Default(None))
		/** Database column tarped SqlType(BIT), Default(None) */
		val tarped: Rep[Option[Boolean]] = column[Option[Boolean]]("tarped", O.Default(None))
		/** Database column estimate_scope_requirement SqlType(TEXT), Default(None) */
		val estimateScopeRequirement: Rep[Option[String]] = column[Option[String]]("estimate_scope_requirement", O.Default(None))
		/** Database column special_instructions SqlType(TEXT), Default(None) */
		val specialInstructions: Rep[Option[String]] = column[Option[String]]("special_instructions", O.Default(None))
		/** Database column status SqlType(INT), Default(1) */
		val status: Rep[Int] = column[Int]("status", O.Default(1))
		/** Database column inspector_id SqlType(INT UNSIGNED), Default(None) */
		val inspectorId: Rep[Option[Int]] = column[Option[Int]]("inspector_id", O.Default(None))
		/** Database column inspection_status SqlType(INT), Default(1) */
		val inspectionStatus: Rep[Int] = column[Int]("inspection_status", O.Default(1))
		/** Database column inspection_type SqlType(INT UNSIGNED), Default(None) */
		val inspectionType: Rep[Option[Int]] = column[Option[Int]]("inspection_type", O.Default(None))
		/** Database column date_of_inspection SqlType(DATE), Default(None) */
		val dateOfInspection: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("date_of_inspection", O.Default(None))
		/** Database column time_of_inspection SqlType(TIME), Default(None) */
		val timeOfInspection: Rep[Option[java.sql.Time]] = column[Option[java.sql.Time]]("time_of_inspection", O.Default(None))
		/** Database column price SqlType(FLOAT UNSIGNED), Default(None) */
		val price: Rep[Option[Float]] = column[Option[Float]]("price", O.Default(None))
		/** Database column is_generated_pdf SqlType(BIT), Default(Some(false)) */
		val isGeneratedPdf: Rep[Option[Boolean]] = column[Option[Boolean]]("is_generated_pdf", O.Default(Some(false)))
		/** Database column last_generated SqlType(DATETIME), Default(None) */
		val lastGenerated: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_generated", O.Default(None))
		/** Database column generate_report_status SqlType(VARCHAR), Length(30,true), Default(None) */
		val generateReportStatus: Rep[Option[String]] = column[Option[String]]("generate_report_status", O.Length(30, varying = true), O.Default(None))
		/** Database column comments SqlType(TEXT), Default(None) */
		val comments: Rep[Option[String]] = column[Option[String]]("comments", O.Default(None))
		/** Database column commenter_id SqlType(INT UNSIGNED), Default(None) */
		val commenterId: Rep[Option[Int]] = column[Option[Int]]("commenter_id", O.Default(None))
		/** Database column latitude SqlType(DOUBLE), Default(None) */
		val latitude: Rep[Option[Double]] = column[Option[Double]]("latitude", O.Default(None))
		/** Database column longtitude SqlType(DOUBLE), Default(None) */
		val longtitude: Rep[Option[Double]] = column[Option[Double]]("longtitude", O.Default(None))
		/** Database column pdfLoc SqlType(VARCHAR), Length(100,true), Default(None) */
		val pdfloc: Rep[Option[String]] = column[Option[String]]("pdfLoc", O.Length(100, varying = true), O.Default(None))


		/** Foreign key referencing InspectionTypes (database name fk_inspection_types) */
		// lazy val inspectionTypesFk = foreignKey("fk_inspection_types", `type` :: HNil, InspectionTypes)(r => Rep.Some(r.id) :: HNil, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
		/** Foreign key referencing Users (database name fk_comment_to_user) */
		lazy val usersFk = foreignKey("fk_comment_to_user", commenterId :: HNil, users)(r => Rep.Some(r.id) :: HNil, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)

	}

	/** Collection-like TableQuery object for table WorkOrders */
	lazy val WorkOrders = new TableQuery(tag => new WorkOrders(tag))

	/**
	 *
	 * @return
	 */
	def db: Database = Database.forDataSource(DB.getDataSource());

	/**
	 * Delete a workorder
	 * @param id
	 */
	def delete(id: Long) {
		val action = WorkOrders.filter(_.id === id).delete
		val count = db.run(action)
	}

	def update(id: Long, w: WorkOrder): Unit = {
		val action = WorkOrders.filter(_.id === id)
		db.run(action.update(w))
	}

	case class baseCounts(basic: Int, expert: Int, total: Int, _new: Int)
	case class dailyCounts(today: baseCounts, tomorrow: baseCounts, yesterday: baseCounts)
	case class weeklyCounts(week: baseCounts, next: baseCounts, last: baseCounts)
	case class monthlyCounts(month: baseCounts, next: baseCounts)
	case class yearlyCounts(year: baseCounts, last: baseCounts)

	case class Counts(daily: dailyCounts,
	                  weekly: weeklyCounts,
	                  monthly: monthlyCounts,
	                  yearly: yearlyCounts)

	implicit val countsWrites: Writes[baseCounts] = (
		(JsPath \ "basic").write[Int] and
			(JsPath \ "expert").write[Int] and
			(JsPath \ "total").write[Int] and
			(JsPath \ "new").write[Int]
		)(unlift(baseCounts.unapply))

	implicit val dailyCountsWrites: Writes[dailyCounts] = (
		(JsPath \ "today").write[baseCounts] and
			(JsPath \ "tomorrow").write[baseCounts] and
			(JsPath \ "yesterday").write[baseCounts]
		)(unlift(dailyCounts.unapply))

	implicit val weeklyCountsWrites: Writes[weeklyCounts] = (
		(JsPath \ "week").write[baseCounts] and
			(JsPath \ "next").write[baseCounts] and
			(JsPath \ "last").write[baseCounts]
		)(unlift(weeklyCounts.unapply))

	implicit val monthlyCountWrites: Writes[monthlyCounts] = (
		(JsPath \ "month").write[baseCounts] and
			(JsPath \ "next").write[baseCounts]
		)(unlift(monthlyCounts.unapply))

	implicit val yearlyCountsWrites: Writes[yearlyCounts] = (
		(JsPath \ "year").write[baseCounts] and
			(JsPath \ "last").write[baseCounts]
		)(unlift(yearlyCounts.unapply))

	implicit val WorkOrderCountsWrites: Writes[Counts] = (
		(JsPath \ "daily").write[dailyCounts] and
			(JsPath \ "weekly").write[weeklyCounts] and
			(JsPath \ "monthly").write[monthlyCounts] and
			(JsPath \ "yearly").write[yearlyCounts]
		)(unlift(Counts.unapply))

	implicit val baseCountsResult = GetResult(r => baseCounts(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()))
	implicit val basicCountsResult = GetResult(r => dailyCounts(r.<<, r.<<, r.<<))
	implicit val expertCountsResult = GetResult(r => weeklyCounts(r.<<, r.<<, r.<<))
	implicit val totalCountsResult = GetResult(r => monthlyCounts(r.<<, r.<<))
	implicit val newCountsResult = GetResult(r => yearlyCounts(r.<<, r.<<))
	implicit val getCountsResult = GetResult(r => Counts(r.<<, r.<<, r.<<, r.<<))

	def counts(): Future[Counts] = {
		val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
		val DAY_IN_MS: Long = 1000 * 60 * 60 * 24
		val oneWeekAgo = new java.sql.Date(System.currentTimeMillis() - (7 * DAY_IN_MS))

		//    sum(case when date_of_inspection > '2015-12-31' then 1 else 0 end) weekCount,
		//    sum(case when date_of_inspection > $oneWeekAgo and date_of_inspection < now() then 1 else 0 end) lastWeek

		val calendar = Calendar.getInstance()
		val today = new java.sql.Date(java.util.Calendar.getInstance().getTimeInMillis());
		val year = calendar.get(Calendar.YEAR) + "-01-01"
		val tomorrow = new java.sql.Date(System.currentTimeMillis() + DAY_IN_MS)
		val yesterday = new java.sql.Date(System.currentTimeMillis() - DAY_IN_MS)

		// First day of week
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek())
		val firstDayOfWeek = format.format(calendar.getTime())
		val firstDayOfLastWeek = new java.sql.Date(calendar.getTime().getTime() - (7 * DAY_IN_MS))
		val firstDayOfNextWeek = new java.sql.Date(calendar.getTime().getTime() + (7 * DAY_IN_MS))

		// Last day of week
		calendar.set(Calendar.DAY_OF_WEEK, 7)
		val lastDayOfWeek = format.format(calendar.getTime())
		val lastDayOfLastWeek = new java.sql.Date(calendar.getTime().getTime() - (7 * DAY_IN_MS))
		val lastDayOfNextWeek = new java.sql.Date(calendar.getTime().getTime() + (7 * DAY_IN_MS))

		// First day of the month
		val month = Calendar.getInstance(); // this takes current date
		month.set(Calendar.DAY_OF_MONTH, 1)
		val firstDayOfMonth = format.format(month.getTime())
		month.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		val lastDayOfMonth = format.format(month.getTime())
		println(firstDayOfMonth, lastDayOfMonth)

		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		val nextMonthFirstDay = format.format(calendar.getTime())
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		val nextMonthLastDay = format.format(calendar.getTime())

		println(nextMonthFirstDay, nextMonthLastDay)

		calendar.add(Calendar.MONTH, -2)
		calendar.set(Calendar.DATE, 1)
		val firstDayOfLastMonth = calendar.getTime()

		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		val lastDayOfLastMonth = calendar.getTime()

		println(firstDayOfLastMonth, lastDayOfLastMonth)


		val action =
			sql"""
           select
           sum(case when date_of_inspection LIKE $today AND type = 0 then 1 else 0 end) basic_today,
           sum(case when date_of_inspection LIKE $today AND type = 1 then 1 else 0 end) expert_today,
           sum(case when date_of_inspection LIKE $today then 1 else 0 end) total_today,
           sum(case when date_of_inspection LIKE $today AND status = 1 then 1 else 0 end) new_today,

           sum(case when date_of_inspection LIKE $tomorrow AND type = 0 then 1 else 0 end) basic_tomorrow,
           sum(case when date_of_inspection LIKE $tomorrow AND type = 1 then 1 else 0 end) expert_tomorrow,
           sum(case when date_of_inspection LIKE $tomorrow then 1 else 0 end) total_tomorrow,
           sum(case when date_of_inspection LIKE $tomorrow AND status = 1 then 1 else 0 end) new_tomorrow,

           sum(case when date_of_inspection LIKE $yesterday AND type = 0 then 1 else 0 end) basic_yesterday,
           sum(case when date_of_inspection LIKE $yesterday AND type = 1 then 1 else 0 end) expert_yesterday,
           sum(case when date_of_inspection LIKE $yesterday then 1 else 0 end) total_yesterday,
           sum(case when date_of_inspection LIKE $yesterday AND status = 1 then 1 else 0 end) new_yesterday,

           sum(case when date_of_inspection >= $firstDayOfWeek AND date_of_inspection <= $lastDayOfWeek AND type = 0 then 1 else 0 end) basic_this_week,
           sum(case when date_of_inspection >= $firstDayOfWeek AND date_of_inspection <= $lastDayOfWeek AND type = 1 then 1 else 0 end) expert_this_week,
           sum(case when date_of_inspection >= $firstDayOfWeek AND date_of_inspection <= $lastDayOfWeek then 1 else 0 end) total_this_week,
           sum(case when date_of_inspection >= $firstDayOfWeek AND date_of_inspection <= $lastDayOfWeek AND status = 1 then 1 else 0 end) new_this_week,

           sum(case when date_of_inspection >= $firstDayOfNextWeek AND date_of_inspection <= $lastDayOfNextWeek AND type = 0 then 1 else 0 end) basic_next_week,
           sum(case when date_of_inspection >= $firstDayOfNextWeek AND date_of_inspection <= $lastDayOfNextWeek AND type = 1 then 1 else 0 end) expert_next_week,
           sum(case when date_of_inspection >= $firstDayOfNextWeek AND date_of_inspection <= $lastDayOfNextWeek then 1 else 0 end) total_next_week,
           sum(case when date_of_inspection >= $firstDayOfNextWeek AND date_of_inspection <= $lastDayOfNextWeek AND status=1  then 1 else 0 end) new_next_week,

            sum(case when date_of_inspection >= $firstDayOfLastWeek AND date_of_inspection <= $lastDayOfLastWeek AND type = 0 then 1 else 0 end) basic_last_week,
            sum(case when date_of_inspection >= $firstDayOfLastWeek AND date_of_inspection <= $lastDayOfLastWeek AND type = 1 then 1 else 0 end) expert_last_week,
            sum(case when date_of_inspection >= $firstDayOfLastWeek AND date_of_inspection <= $lastDayOfLastWeek then 1 else 0 end) total_last_week,
            sum(case when date_of_inspection >= $firstDayOfLastWeek AND date_of_inspection <= $lastDayOfLastWeek AND status=1 then 1 else 0 end) new_last_week,

            sum(case when date_of_inspection >= $firstDayOfMonth AND date_of_inspection <= $lastDayOfMonth AND type = 0 then 1 else 0 end) basic_this_month,
            sum(case when date_of_inspection >= $firstDayOfMonth AND date_of_inspection <= $lastDayOfMonth AND type = 1 then 1 else 0 end) expert_this_month,
            sum(case when date_of_inspection >= $firstDayOfMonth AND date_of_inspection <= $lastDayOfMonth then 1 else 0 end) total_this_month,
            sum(case when date_of_inspection >= $firstDayOfMonth AND date_of_inspection <= $lastDayOfMonth AND status = 1 then 1 else 0 end) new_this_month,

            sum(case when date_of_inspection >= $nextMonthFirstDay AND date_of_inspection <= $nextMonthLastDay AND type = 0 then 1 else 0 end) basic_next_month,
            sum(case when date_of_inspection >= $nextMonthFirstDay AND date_of_inspection <= $nextMonthLastDay AND type = 1 then 1 else 0 end) expert_next_month,
            sum(case when date_of_inspection >= $nextMonthFirstDay AND date_of_inspection <= $nextMonthLastDay then 1 else 0 end) total_next_month,
		    sum(case when date_of_inspection >= $nextMonthFirstDay AND date_of_inspection <= $nextMonthLastDay AND status = 1 then 1 else 0 end) new_next_month,

           sum(case when date_of_inspection > $year AND type = 0 then 1 else 0 end) basic_year,
           sum(case when date_of_inspection > $year AND type = 1 then 1 else 0 end) expert_year,
           sum(case when date_of_inspection > $year AND type != 2 then 1 else 0 end) total_year,
           sum(case when date_of_inspection > $year AND status = 1 then 1 else 0 end) new_year,

           sum(case when date_of_inspection < $year AND type = 0 then 1 else 0 end) basic_last_year,
           sum(case when date_of_inspection < $year AND type = 1 then 1 else 0 end) expert_last_year,
           sum(case when date_of_inspection < $year AND type != 2 then 1 else 0 end) total_last_year,
           sum(case when date_of_inspection < $year AND status = 1 then 1 else 0 end) new_last_year

           from work_orders""".as[Counts].head
		db.run(action)
	}


}
