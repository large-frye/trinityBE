
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/frye/Documents/scala/trinityBE/conf/routes
// @DATE:Sat Oct 03 09:26:25 EDT 2015

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:13
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def at: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.at",
      """
        function(path,file) {
        
          if (path == """ + implicitly[JavascriptLiteral[String]].to("/public/stylesheets") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "stylesheets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
          }
        
          if (path == """ + implicitly[JavascriptLiteral[String]].to("/public/javascripts") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "javascripts/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
          }
        
          if (path == """ + implicitly[JavascriptLiteral[String]].to("/public/images") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "images/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
          }
        
        }
      """
    )
  
  }

  // @LINE:8
  class ReverseWorkorders(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def all: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Workorders.all",
      """
        function(start,limit) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "workorders/all/" + (""" + implicitly[PathBindable[Int]].javascriptUnbind + """)("start", start) + "/" + (""" + implicitly[PathBindable[Int]].javascriptUnbind + """)("limit", limit)})
        }
      """
    )
  
    // @LINE:8
    def find: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Workorders.find",
      """
        function(id) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "workorders/find/" + (""" + implicitly[PathBindable[Long]].javascriptUnbind + """)("id", id)})
        }
      """
    )
  
    // @LINE:10
    def counts: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Workorders.counts",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "workorders/counts"})
        }
      """
    )
  
  }

  // @LINE:5
  class ReverseApplication(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Application.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }


}