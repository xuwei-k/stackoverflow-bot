package stackoverflow_bot

import scala.util.control.Exception.allCatch
import java.io.File
import java.net.URL

object Main{
  private def stackoverflow(tag: String, lang: Option[String]): URL =
    lang match {
      case Some(l) => 
        new URL("http://" + l + ".stackoverflow.com/feeds/tag?tagnames=" + tag + "&sort=newest")
      case None =>
        new URL("http://stackoverflow.com/feeds/tag?tagnames=" + tag + "&sort=newest")
    }

  final val defaultConfigName = "config.scala"

  def main(args: Array[String]){
    val file = new File(
      allCatch.opt(args.head).getOrElse(defaultConfigName)
    )
    run(file)
  }

  def run(file: File): Unit = {
    val env = Env.fromConfigFile(file)
    import env._, env.config._

    val firstData = getEntries(tag, lang, blockUsers)
    db.insert(firstData.map{_.link}.toList)
    printDateTime()
    println("first insert data = " + firstData)
    firstData.take(firstTweetCount).reverseIterator.foreach { entry =>
      Thread.sleep(tweetInterval.toMillis)
      client.tweet(entry.tweetString(hashTags))
    }
    loop(env)
  }

  @annotation.tailrec
  def loop(env: Env): Unit = {
    import env._, env.config._
    allCatchPrintStackTrace{
      Thread.sleep(interval.toMillis)
      val oldIds = db.selectAll
      val newData = getEntries(tag, lang, blockUsers).filterNot{a => oldIds.contains(a.link)}
      db.insert(newData.map{_.link}.toList)
      newData.reverseIterator.foreach { e =>
        Thread.sleep(env.config.tweetInterval.toMillis)
        env.client.tweet(e.tweetString(hashTags))
      }
    }
    loop(env.reload)
  }

  def getEntries(tag: String, lang: Option[String], blockUsers: Set[Long]): Seq[Item] = {
    val x = scala.xml.XML.load(stackoverflow(tag, lang))
    (x \ "entry").map(Item.apply).filterNot(item => blockUsers(item.authorId))
  }

  def printDateTime(): Unit = {
    val df = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm")
    println(df.format(new java.util.Date))
  }
}
