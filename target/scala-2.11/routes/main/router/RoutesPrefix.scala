
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/frye/Documents/scala/trinityBE/conf/routes
// @DATE:Mon Oct 26 22:05:29 EDT 2015


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
