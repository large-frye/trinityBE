
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/frye/Documents/scala/trinityBE/conf/routes
// @DATE:Sat Oct 03 09:26:25 EDT 2015

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package controllers {

  // @LINE:13
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def at(path:String, file:String): Call = {
    
      (path: @unchecked, file: @unchecked) match {
      
        // @LINE:13
        case (path, file) if path == "/public/stylesheets" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/stylesheets")))
          Call("GET", _prefix + { _defaultPrefix } + "stylesheets/" + implicitly[PathBindable[String]].unbind("file", file))
      
        // @LINE:14
        case (path, file) if path == "/public/javascripts" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/javascripts")))
          Call("GET", _prefix + { _defaultPrefix } + "javascripts/" + implicitly[PathBindable[String]].unbind("file", file))
      
        // @LINE:15
        case (path, file) if path == "/public/images" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/images")))
          Call("GET", _prefix + { _defaultPrefix } + "images/" + implicitly[PathBindable[String]].unbind("file", file))
      
      }
    
    }
  
  }

  // @LINE:8
  class ReverseWorkorders(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def all(start:Int, limit:Int): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "workorders/all/" + implicitly[PathBindable[Int]].unbind("start", start) + "/" + implicitly[PathBindable[Int]].unbind("limit", limit))
    }
  
    // @LINE:8
    def find(id:Long): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "workorders/find/" + implicitly[PathBindable[Long]].unbind("id", id))
    }
  
    // @LINE:10
    def counts(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "workorders/counts")
    }
  
  }

  // @LINE:5
  class ReverseApplication(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def index(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix)
    }
  
  }


}