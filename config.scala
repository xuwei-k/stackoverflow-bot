import stackoverflow_bot._

import scala.concurrent.duration._

new Config{
  val tag = "sbt"
  val interval = 1.hours

  val twitter = new TwitterSettings{
    val consumerKey       = ""
    val consumerSecret    = ""
    val accessToken       = ""
    val accessTokenSecret = ""
  }
}

