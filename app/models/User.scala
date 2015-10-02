package models

import slick.driver.MySQLDriver.api._

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait User {
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  case class User(
                   id: Int,
                   email: String,
                   username: String = "",
                   password: String,
                   logins: Int = 0,
                   lastLogin: Option[Int] = None,
                   createdOnUtc: Option[java.sql.Timestamp] = None,
                   status: Option[Boolean] = None)


  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def email = column[String]("email", O.Length(254, varying = true))

    def username = column[String]("username", O.Length(32, varying = true), O.Default(""))

    def password = column[String]("password", O.Length(64, varying = true))

    def logins = column[Int]("logins", O.Default(0))

    def lastLogin = column[Option[Int]]("last_login", O.Default(None))

    def createdOnUtc = column[Option[java.sql.Timestamp]]("created_on_utc", O.Default(None))

    def status = column[Option[Boolean]]("status", O.Default(None))

    def * = (id, email, username, password, logins, lastLogin, createdOnUtc, status) <>(User.tupled, User.unapply)
  }

  lazy val users = new TableQuery(tag => new Users(tag))
}




