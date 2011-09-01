object Try3 {
  def main(args: Array[String]) {
    import org.scalacheck._

    def serialize(string: String) = string.getBytes("UTF-8")
    def deserialize(bytes: Array[Byte]) = new String(bytes, "UTF-8")

    val prop_serializes_all_strings = Prop.forAll { s: String =>
      s == deserialize(serialize(s))
    }

    prop_serializes_all_strings.check
  }
}
