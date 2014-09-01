package stackoverflow_bot

import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

final case class TweetClient(conf: TwitterSettings) {

  val t = {
    val c = new ConfigurationBuilder
      c.setDebugEnabled(true)
      .setOAuthConsumerKey(conf.consumerKey)
      .setOAuthConsumerSecret(conf.consumerSecret)
      .setOAuthAccessToken(conf.accessToken)
      .setOAuthAccessTokenSecret(conf.accessTokenSecret)

    new TwitterFactory(c.build()).getInstance()
  }

  def tweet(str:String): Unit = {
    allCatchPrintStackTrace{
      t.updateStatus(str)
    }
  }

}


