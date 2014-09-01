package stackoverflow_bot

import java.io.File

final case class Env(
  config: Config,
  db: DB[ITEM_URL],
  client: TweetClient,
  private val previousConfig: String,
  private val confFile: File
){

  def reload: Env = try{
    val confString = scala.io.Source.fromFile(confFile).mkString
    if(previousConfig != confString){
      Main.printDateTime()
      println("reload config file")
      val newConfig = Eval[Config](confString)
      this.copy(config = newConfig, previousConfig = confString, client = TweetClient(newConfig.twitter))
    }else{
      this
    }
  }catch{
    case e: Throwable =>
      Main.printDateTime()
      e.printStackTrace()
      this
  }
}

object Env {
  def fromConfigFile(file: File): Env = {
    val confString = scala.io.Source.fromFile(file).mkString
    val config = Eval[Config](confString)
    val db = new DB[ITEM_URL](config.dbSize)
    val client = TweetClient(config.twitter)
    Env(config, db, client, confString, file)
  }
}
