package io.regadas.scio.contextual

import contextual._

object gcs {
  object GcsPathParser extends Verifier[String] {
    private[this] val GcsUriRegExp = "gs://[a-z0-9][-_a-z0-9.]+[a-z0-9](/.*)?".r

    override def check(string: String): Either[(Int, String), String] =
      Either.cond(
        string.matches(GcsUriRegExp.regex),
        string,
        (0, s"invalid uri format: $GcsUriRegExp")
      )
  }

  implicit class GcsPathContext(sc: StringContext) {
    val gcs = Prefix(GcsPathParser, sc)
  }
}
