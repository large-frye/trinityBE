
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/frye/Documents/scala/trinityBE/conf/routes
// @DATE:Sat Oct 03 09:26:25 EDT 2015


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
