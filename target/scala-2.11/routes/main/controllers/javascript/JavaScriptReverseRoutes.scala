
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/andrewfrye/Documents/trinityBE/conf/routes
// @DATE:Mon Nov 02 21:27:57 EST 2015

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:14
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
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

  
    // @LINE:11
    def findByDate: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Workorders.findByDate",
      """
        function(filterType,limit,start,amount) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "workorders/date/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("filterType", encodeURIComponent(filterType)) + "/" + (""" + implicitly[PathBindable[Int]].javascriptUnbind + """)("limit", limit) + "/" + (""" + implicitly[PathBindable[Int]].javascriptUnbind + """)("start", start) + "/" + (""" + implicitly[PathBindable[Int]].javascriptUnbind + """)("amount", amount)})
        }
      """
    )
  
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