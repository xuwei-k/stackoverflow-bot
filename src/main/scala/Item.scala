package stackoverflow_bot

final case class Item private(
  link        :ITEM_URL,
  title       :String,
  description :String,
  authorId    :Long
) {
  import Item._

  def tweetString(hashTags: Set[String] = Set.empty): String = {
    val tags = hashTags.collect{case s if ! s.isEmpty => "#" + s }.mkString(" ")
    Iterator(
      link, tags, escape(title), escape(description.replace("            <p>", "").replace("<pre><code>",""))
    ).mkString("\n").take(LIMIT)
  }
}

object Item{
  final val LIMIT = 140

  private[this] val escapeMap = Map(
    "@" -> "",
    "#" -> "♯"
  )

  def escape(str: String): String =
    escapeMap.foldLeft(str){case (s, (k, v)) => s.replace(k, v)}

  def apply(x: scala.xml.Node): Item = Item(
    (x \ "id").text,
    (x \ "title").text,
    (x \ "summary").text,
    (x \ "author" \ "uri").text.split('/').last.toLong
  )
}
