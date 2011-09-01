object Solution2 {
  def main(args: Array[String]) {
    import java.nio.{ ByteBuffer, CharBuffer }
    import java.nio.charset.Charset
    import org.scalacheck._, Prop._

    def encoder = Charset.forName("UTF-8").newEncoder
    def decoder = Charset.forName("UTF-8").newDecoder

    val UnicodeLeadingSurrogate = '\uD800' to '\uDBFF'
    val UnicodeTrailingSurrogate = '\uDC00' to '\uDFFF'
    val UnicodeBasicMultilingualPlane = ('\u0000' to '\uFFFF').diff(UnicodeLeadingSurrogate).diff(UnicodeTrailingSurrogate)

    val unicodeCharacterBasicMultilingualPlane: Gen[String] = Gen.oneOf(UnicodeBasicMultilingualPlane).map(_.toString)
    val unicodeCharacterSupplementaryPlane: Gen[String] = for {
      c1 <- Gen.oneOf(UnicodeLeadingSurrogate)
      c2 <- Gen.oneOf(UnicodeTrailingSurrogate)
    } yield {
      c1.toString + c2.toString
    }

    val unicodeCharacter = Gen.frequency(
      9 -> unicodeCharacterBasicMultilingualPlane,
      1 -> unicodeCharacterSupplementaryPlane)

    val unicodeString = Gen.listOf(unicodeCharacter).map(_.mkString)

    def serialize(string: String) = {
      val bytes = encoder.encode(CharBuffer.wrap(string))
      bytes.array.slice(bytes.position, bytes.limit)
    }
    def deserialize(bytes: Array[Byte]) = decoder.decode(ByteBuffer.wrap(bytes)).toString

    def decode(string: String) = "characters = " + string.map(_.toInt).mkString(",")

    val prop_serializes_all_strings = Prop.forAll(unicodeString) { s: String =>
      decode(s) |: s == deserialize(serialize(s))
    }

    prop_serializes_all_strings.check
  }
}
