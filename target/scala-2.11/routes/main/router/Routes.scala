
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/andrewfrye/Documents/trinityBE/conf/routes
// @DATE:Mon Nov 02 21:27:57 EST 2015

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

object Routes extends Routes

class Routes extends GeneratedRouter {

  import ReverseRouteContext.empty

  override val errorHandler: play.api.http.HttpErrorHandler = play.api.http.LazyHttpErrorHandler

  private var _prefix = "/"

  def withPrefix(prefix: String): Routes = {
    _prefix = prefix
    router.RoutesPrefix.setPrefix(prefix)
    
    this
  }

  def prefix: String = _prefix

  lazy val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation: Seq[(String, String, String)] = List(
    ("""GET""", prefix, """controllers.Application.index()"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """workorders/find/$id<[^/]+>""", """controllers.Workorders.find(id:Long)"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """workorders/all/$start<[^/]+>/$limit<[^/]+>""", """controllers.Workorders.all(start:Int, limit:Int)"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """workorders/counts""", """controllers.Workorders.counts"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """workorders/date/$filterType<[^/]+>/$limit<[^/]+>/$start<[^/]+>/$amount<[^/]+>""", """controllers.Workorders.findByDate(filterType:String, limit:Int, start:Int, amount:Int)"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """stylesheets/$file<.+>""", """controllers.Assets.at(path:String = "/public/stylesheets", file:String)"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """javascripts/$file<.+>""", """controllers.Assets.at(path:String = "/public/javascripts", file:String)"""),
    ("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """images/$file<.+>""", """controllers.Assets.at(path:String = "/public/images", file:String)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:5
  private[this] lazy val controllers_Application_index0_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_Application_index0_invoker = createInvoker(
    controllers.Application.index(),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Application",
      "index",
      Nil,
      "GET",
      """""",
      this.prefix + """"""
    )
  )

  // @LINE:8
  private[this] lazy val controllers_Workorders_find1_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("workorders/find/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_Workorders_find1_invoker = createInvoker(
    controllers.Workorders.find(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Workorders",
      "find",
      Seq(classOf[Long]),
      "GET",
      """ Workorders""",
      this.prefix + """workorders/find/$id<[^/]+>"""
    )
  )

  // @LINE:9
  private[this] lazy val controllers_Workorders_all2_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("workorders/all/"), DynamicPart("start", """[^/]+""",true), StaticPart("/"), DynamicPart("limit", """[^/]+""",true)))
  )
  private[this] lazy val controllers_Workorders_all2_invoker = createInvoker(
    controllers.Workorders.all(fakeValue[Int], fakeValue[Int]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Workorders",
      "all",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      """""",
      this.prefix + """workorders/all/$start<[^/]+>/$limit<[^/]+>"""
    )
  )

  // @LINE:10
  private[this] lazy val controllers_Workorders_counts3_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("workorders/counts")))
  )
  private[this] lazy val controllers_Workorders_counts3_invoker = createInvoker(
    controllers.Workorders.counts,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Workorders",
      "counts",
      Nil,
      "GET",
      """""",
      this.prefix + """workorders/counts"""
    )
  )

  // @LINE:11
  private[this] lazy val controllers_Workorders_findByDate4_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("workorders/date/"), DynamicPart("filterType", """[^/]+""",true), StaticPart("/"), DynamicPart("limit", """[^/]+""",true), StaticPart("/"), DynamicPart("start", """[^/]+""",true), StaticPart("/"), DynamicPart("amount", """[^/]+""",true)))
  )
  private[this] lazy val controllers_Workorders_findByDate4_invoker = createInvoker(
    controllers.Workorders.findByDate(fakeValue[String], fakeValue[Int], fakeValue[Int], fakeValue[Int]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Workorders",
      "findByDate",
      Seq(classOf[String], classOf[Int], classOf[Int], classOf[Int]),
      "GET",
      """""",
      this.prefix + """workorders/date/$filterType<[^/]+>/$limit<[^/]+>/$start<[^/]+>/$amount<[^/]+>"""
    )
  )

  // @LINE:14
  private[this] lazy val controllers_Assets_at5_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("stylesheets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at5_invoker = createInvoker(
    controllers.Assets.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """ Assets""",
      this.prefix + """stylesheets/$file<.+>"""
    )
  )

  // @LINE:15
  private[this] lazy val controllers_Assets_at6_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("javascripts/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at6_invoker = createInvoker(
    controllers.Assets.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """""",
      this.prefix + """javascripts/$file<.+>"""
    )
  )

  // @LINE:16
  private[this] lazy val controllers_Assets_at7_route: Route.ParamsExtractor = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("images/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at7_invoker = createInvoker(
    controllers.Assets.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """""",
      this.prefix + """images/$file<.+>"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:5
    case controllers_Application_index0_route(params) =>
      call { 
        controllers_Application_index0_invoker.call(controllers.Application.index())
      }
  
    // @LINE:8
    case controllers_Workorders_find1_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_Workorders_find1_invoker.call(controllers.Workorders.find(id))
      }
  
    // @LINE:9
    case controllers_Workorders_all2_route(params) =>
      call(params.fromPath[Int]("start", None), params.fromPath[Int]("limit", None)) { (start, limit) =>
        controllers_Workorders_all2_invoker.call(controllers.Workorders.all(start, limit))
      }
  
    // @LINE:10
    case controllers_Workorders_counts3_route(params) =>
      call { 
        controllers_Workorders_counts3_invoker.call(controllers.Workorders.counts)
      }
  
    // @LINE:11
    case controllers_Workorders_findByDate4_route(params) =>
      call(params.fromPath[String]("filterType", None), params.fromPath[Int]("limit", None), params.fromPath[Int]("start", None), params.fromPath[Int]("amount", None)) { (filterType, limit, start, amount) =>
        controllers_Workorders_findByDate4_invoker.call(controllers.Workorders.findByDate(filterType, limit, start, amount))
      }
  
    // @LINE:14
    case controllers_Assets_at5_route(params) =>
      call(Param[String]("path", Right("/public/stylesheets")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at5_invoker.call(controllers.Assets.at(path, file))
      }
  
    // @LINE:15
    case controllers_Assets_at6_route(params) =>
      call(Param[String]("path", Right("/public/javascripts")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at6_invoker.call(controllers.Assets.at(path, file))
      }
  
    // @LINE:16
    case controllers_Assets_at7_route(params) =>
      call(Param[String]("path", Right("/public/images")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at7_invoker.call(controllers.Assets.at(path, file))
      }
  }
}