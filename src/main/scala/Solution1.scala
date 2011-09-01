object Solution1 {
  def main(args: Array[String]) {
    import java.nio.{ ByteBuffer, CharBuffer }
    import java.nio.charset.Charset
    import org.scalacheck._, Prop._

    def encoder = Charset.forName("UTF-8").newEncoder
    def decoder = Charset.forName("UTF-8").newDecoder

    def serialize(string: String) = {
      val bytes = encoder.encode(CharBuffer.wrap(string))
      bytes.array.slice(bytes.position, bytes.limit)
    }
    def deserialize(bytes: Array[Byte]) = decoder.decode(ByteBuffer.wrap(bytes)).toString

    def decode(string: String) = "characters = " + string.map(_.toInt).mkString(",")

    val prop_serializes_all_strings = Prop.forAll { s: String =>
      encoder.canEncode(s) ==> (decode(s) |: s == deserialize(serialize(s)))
    }

    prop_serializes_all_strings.check(Test.Params(minSuccessfulTests = 50, maxDiscardedTests = 1000))
  }
}
