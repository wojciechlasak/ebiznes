name := "ebiznes_app"
 
version := "1.0" 
      
lazy val `ebiznes_app` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "mysql" % "mysql-connector-java" % "5.1.46"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      