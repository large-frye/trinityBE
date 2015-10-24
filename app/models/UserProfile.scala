package models

import slick.driver.MySQLDriver.api._

/**
 * Created by frye on 10/12/15.
 */
trait UserProfile {
	case class UserProfile(
	                      id: Int,
	                      userId: Int,
	                      firstName: Option[String],
	                      lastName: Option[String],
	                      phone: Option[String],
	                      geographicRegion: Option[String],
	                      insuranceCompany: Option[String],
	                      color: Option[String])

	class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "profiles") {
		def id = column[Int]("id", O.AutoInc, O.PrimaryKey)
		def userId = column[Int]("user_id", O.Default(1))
		def firstName = column[Option[String]]("first_name", O.Default(None))
		def lastName = column[Option[String]]("last_name", O.Default(None))
		def phone = column[Option[String]]("phone", O.Default(None))
		def geographicRegion = column[Option[String]]("geographic_region", O.Default(None))
		def insuranceCompany = column[Option[String]]("insurance_company", O.Default(None))
		def color = column[Option[String]]("color", O.Default(None))
		def * = (id, userId, firstName, lastName, phone, geographicRegion, insuranceCompany, color) <> (UserProfile.tupled, UserProfile.unapply)
	}

	lazy val UserProfiles = new TableQuery(tag => new UserProfiles(tag))
}
