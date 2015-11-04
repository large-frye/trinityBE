
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/andrewfrye/Documents/trinityBE/conf/routes
// @DATE:Mon Nov 02 21:27:57 EST 2015


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
