package io.circe.hygiene

import io.circe.{ Decoder, Encoder, Json }
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.literal._

case class Foo(s: String, i: Int, o: Option[Double], b: List[Boolean])

/**
 * Compilation tests for macro hygiene.
 *
 * Fake definitions suggested by Jason Zaugg.
 */
object HygieneTests {
  val scala, Any, String, Unit = ()
  trait scala; trait Any; trait String; trait Unit

  val autoDerivedFooEncoder: Encoder[Foo] = Encoder[Foo]
  val derivedFooEncoder: Encoder[Foo] = deriveEncoder[Foo]

  val autoDerivedFooDecoder: Decoder[Foo] = Decoder[Foo]
  val derivedFooDecoder: Decoder[Foo] = deriveDecoder[Foo]

  val json: Json = json"""
    {
      "foo": {
        "s": "abcdef",
        "i": 10001,
        "o": 10.01,
        "b": [ true, false ]
      }
    }
  """
}
