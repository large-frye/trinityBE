package models

import java.util.Calendar

import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}
import slick.driver.MySQLDriver.api._
import slick.jdbc.GetResult
import play.api.Play.current
import scala.concurrent.Future

/**
 * Created by frye on 10/2/15.
 */
trait Count {
	import slick.jdbc.{GetResult => GR}

	case class baseCounts(basic: Int, expert: Int, total: Int, _new: Int)
	case class dailyCounts(today: baseCounts, tomorrow: baseCounts, yesterday: baseCounts)
	case class weeklyCounts(week: baseCounts, next: baseCounts, last: baseCounts)
	case class monthlyCounts(month: baseCounts, last: baseCounts, next: baseCounts)
	case class yearlyCounts(year: baseCounts, last: baseCounts)

	def db: Database = Database.forDataSource(DB.getDataSource())

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
			(JsPath \ "last").write[baseCounts] and
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
	implicit val totalCountsResult = GetResult(r => monthlyCounts(r.<<, r.<<, r.<<))
	implicit val newCountsResult = GetResult(r => yearlyCounts(r.<<, r.<<))
	implicit val getCountsResult = GetResult(r => Counts(r.<<, r.<<, r.<<, r.<<))

	def getCounts(): Future[Counts] = {
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

		calendar.add(Calendar.MONTH, 1)
		calendar.set(Calendar.DAY_OF_MONTH, 1)
		val nextMonthFirstDay = format.format(calendar.getTime())
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		val nextMonthLastDay = format.format(calendar.getTime())

		println(nextMonthFirstDay, nextMonthLastDay)

		calendar.add(Calendar.MONTH, -2)
		calendar.set(Calendar.DATE, 1)
		val firstDayOfLastMonth = format.format(calendar.getTime())

		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		val lastDayOfLastMonth = format.format(calendar.getTime())

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

            sum(case when date_of_inspection >= $firstDayOfLastMonth AND date_of_inspection <= $lastDayOfLastMonth AND type = 0 then 1 else 0 end) basic_last_month,
            sum(case when date_of_inspection >= $firstDayOfLastMonth AND date_of_inspection <= $lastDayOfLastMonth AND type = 1 then 1 else 0 end) expert_last_month,
            sum(case when date_of_inspection >= $firstDayOfLastMonth AND date_of_inspection <= $lastDayOfLastMonth then 1 else 0 end) total_last_month,
            sum(case when date_of_inspection >= $firstDayOfLastMonth AND date_of_inspection <= $lastDayOfLastMonth AND status = 1 then 1 else 0 end) new_last_month,

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
