package dao

// Model
import models.User

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
import java.sql.Timestamp


/**
 * Created by frye on 9/9/15.
 */
//  def create(u: User): Future[Option[User]] = {
//    val sql = (users returning users.map(_.*)) += u
//    try db.run(sql)
//    finally db.close
//  }
//
//}

object UsersDAO extends User {
  val profile = slick.driver.MySQLDriver
  import profile.api._

  /**
   *
   * Database
   * @return
   */
  def db: Database = Database.forDataSource(DB.getDataSource());

  /**
   * Filter a user by id.
   * @param id
   * @return
   */
  def findById(id: Int): Future[Option[User]] = {
    val query = users.filter(_.id === id)
    try db.run(query.result.headOption)
    finally db.close()
  }

  /**
   * Find all users
   * @return
   */
  def find(): Future[Seq[UsersDAO.User]] = {
    val q = for (u <- users) yield u
    try db.run(q.result)
    finally db.close
  }

  /********** JSON Read / Writes for a UsersDAO.User **********/

  /**
   * Read/Writes format for java.sql.timestamp
   */
  implicit val rds: Reads[Timestamp] = (__ \ "time").read[Long].map { long => new Timestamp(long) }
  implicit val wrs: Writes[Timestamp] = (__ \ "time").write[Long].contramap { (a: Timestamp) => a.getTime }

  /**
   * User writes for UsersDAO.User
   */
  implicit val userWrites: Writes[UsersDAO.User] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "email").write[String] and
      (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "logins").write[Int] and
      (JsPath \ "lastLogin").writeNullable[Int] and
      (JsPath \ "createdOnUtc").writeNullable[Timestamp] and
      (JsPath \ "status").writeNullable[Boolean]
    )(unlift(UsersDAO.User.unapply))

  /**
   * User reads for UsersDAO.User
   */
  implicit val userReads: Reads[UsersDAO.User] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "email").read[String] and
      (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "logins").read[Int] and
      (JsPath \ "lastLogin").readNullable[Int] and
      (JsPath \ "createdOnUtc").readNullable[Timestamp] and
      (JsPath \ "status").readNullable[Boolean]
    )(UsersDAO.User.apply _)

  /**
   * Format combinator w/ reads and writes for a user
   */
  implicit val userFormat: Format[UsersDAO.User] =
    Format(userReads, userWrites)
}
