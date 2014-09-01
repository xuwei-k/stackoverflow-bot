package object stackoverflow_bot {

  @inline def allCatchPrintStackTrace(body: => Any){
    try{
      val _ = body
    }catch{
      case e: Throwable =>
        Main.printDateTime()
        e.printStackTrace()
    }
  }

  type ITEM_URL = String
}
