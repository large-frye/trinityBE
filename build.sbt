name := "trinity"

version := "1.0"

lazy val `trinity` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , cache , ws,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "com.typesafe.slick" % "slick_2.11" % "3.0.0",
//  "com.typesafe.slick" % "slick-codegen_2.11" % "2.1.0",
  "com.typesafe.play" % "play-slick_2.11" % "1.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value)

libraryDependencies += filters

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

fork in run := false