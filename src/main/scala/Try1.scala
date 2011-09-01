// try1.scala
object Try1 {
  def main(args: Array[String]) {
    def serialize(string: String) = string.getBytes;
    def deserialize(bytes: Array[Byte]) = new String(bytes);
    def test(string: String) {
      val actual = deserialize(serialize(string))
      println(if (string == actual) "OK ".format(string)
      else "FAIL expected  got ".format(string, actual))
    }

    test("")
    test("foo")
    test("the quick brown fox jumps over the lazy dog")
    test("\u2192")
  }
}
