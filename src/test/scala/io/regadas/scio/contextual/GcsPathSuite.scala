package io.regadas.scio.contextual

import io.regadas.scio.contextual.gcs._

class GcsPathSuite extends munit.FunSuite {

  test("valid path") {
    gcs"gs://bucket/object/path"
  }

  test("valid path without schema") {
    gcs"bucket/object/path"
  }

  test("invalid schema") {
    assertNoDiff(
      compileErrors("""gcs"gcs://bucket/object/path""""),
      """|error: invalid uri format: (gs://)?[a-z0-9][-_a-z0-9.]+[a-z0-9](/.*)?
         |gcs"gcs://bucket/object/path"
         |    ^
         |""".stripMargin
    )
  }

  test("invalid bucket name") {
    assertNoDiff(
      compileErrors("""gcs"gs://bu/path""""),
      """|error: invalid uri format: (gs://)?[a-z0-9][-_a-z0-9.]+[a-z0-9](/.*)?
         |gcs"gs://bu/path"
         |    ^
         |""".stripMargin
    )
  }

  test("invalid bucket name without schema") {
    assertNoDiff(
      compileErrors("""gcs"bu/path""""),
      """|error: invalid uri format: (gs://)?[a-z0-9][-_a-z0-9.]+[a-z0-9](/.*)?
         |gcs"bu/path"
         |    ^
         |""".stripMargin
    )
  }

}
