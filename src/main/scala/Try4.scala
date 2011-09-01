// try4.scala
object Try4 {
  def main(args: Array[String]) {
    import org.scalacheck._, Prop._

    def serialize(string: String) = string.getBytes("UTF-8")
    def deserialize(bytes: Array[Byte]) = new String(bytes, "UTF-8")

    def decode(string: String) = "characters = " + string.map(_.toInt).mkString(",")

    val prop_serializes_all_strings = Prop.forAll { s: String =>
      decode(s) |: s == deserialize(serialize(s))
    }

    prop_serializes_all_strings.check
  }
}
