package controllers
import play.api.mvc._


object Application extends Controller {

  def index = Action {
    Ok("Trinity API").withSession(
      "user" -> "a.frye4@gmail.com"
    )
  }

}