
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object main_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.32*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(/*7.17*/title),format.raw/*7.22*/("""</title>
       
    </head>
    <body>
        """),_display_(/*11.10*/content),format.raw/*11.17*/("""
    """),format.raw/*12.5*/("""</body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


}

/**/
object main extends main_Scope0.main
              /*
                  -- GENERATED --
                  DATE: Fri Oct 30 00:28:31 EDT 2015
                  SOURCE: /Users/andrewfrye/Documents/trinityBE/app/views/main.scala.html
                  HASH: 851874d24de8e7f14f5b0e4aa3530a79fa74ecb0
                  MATRIX: 530->1|655->31|683->33|760->84|785->89|861->138|889->145|921->150
                  LINES: 20->1|25->1|27->3|31->7|31->7|35->11|35->11|36->12
                  -- GENERATED --
              */
          