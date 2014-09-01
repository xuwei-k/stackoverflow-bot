package stackoverflow_bot

import scala.concurrent.duration._

abstract class Config{
  val twitter: TwitterSettings
  def tag: String
  val hashTags: Set[String] = Set(tag)
  val tweetInterval: Duration = 1.second
  val interval: Duration
  val dbSize: Int = 100
  val firstTweet: Boolean = false
}

abstract class TwitterSettings{
  val consumerKey: String
  val consumerSecret: String
  val accessToken: String
  val accessTokenSecret: String
}

