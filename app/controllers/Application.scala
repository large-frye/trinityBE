package controllers
import play.api.mvc._


object Application extends Controller {

  def index = Action {
    Ok("Trinity API").withSession(
      "authenticated" -> "a.frye4@gmail.com"
    )
  }

}