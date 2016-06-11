package io.circe

import java.util.UUID
import cats.data._

/**
 * A type class that provides back and forth conversion between values of type `A`
 * and the [[Json]] format. Must obey the laws defined in [[io.circe.tests.CodecLaws]].
 */
trait Codec[A] extends Encoder[A] with Decoder[A]

object Codec {
  def apply[A](implicit instance: Codec[A]): Codec[A] = instance

  implicit def fromEncoderDecoder[A](implicit e: Encoder[A], d: Decoder[A]): Codec[A] =
    new Codec[A] {
      def apply(c: HCursor): Decoder.Result[A] = d(c)
      def apply(a: A): Json = e(a)
    }

  implicit final val codecUUID: Codec[UUID] = new Codec[UUID] {
    final def apply(c: HCursor): Decoder.Result[UUID] = c.focus match {
      case Json.JString(string) if string.length == 36 => try {
        Xor.right(UUID.fromString(string))
      } catch {
        case _: IllegalArgumentException => Xor.left(DecodingFailure("UUID", c.history))
      }
      case _ => Xor.left(DecodingFailure("UUID", c.history))
    }

    final def apply(a: UUID): Json = Json.fromString(a.toString)
  }
}
