package io.regadas.scio.contextual

import io.regadas.scio.contextual.pubsub._

class PubsubSuite extends munit.FunSuite {

  test("valid topic") {
    topic"projects/project-id/topics/topicName"
  }

  test("invalid topic") {
    assertNoDiff(
      compileErrors("""topic"projects/projectId/tropics/topicName""""),
      """|error: invalid format: projects/<project_id>/topics/<name>
         |topic"projects/projectId/tropics/topicName"
         |      ^
         |""".stripMargin
    )
  }

  test("valid subscription") {
    subscription"projects/project-id/subscriptions/subName"
  }

  test("invalid subscription") {
    assertNoDiff(
      compileErrors(
        """subscription"projects/projectId/subscription/subName""""
      ),
      """|error: invalid format: projects/<project_id>/subscriptions/<name>
         |subscription"projects/projectId/subscription/subName"
         |             ^
         |""".stripMargin
    )
  }
}
