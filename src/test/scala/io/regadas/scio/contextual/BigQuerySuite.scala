package io.regadas.scio.contextual

import io.regadas.scio.contextual.bigquery._

class BigQuerySuite extends munit.FunSuite {

  test("spec with invalid projectid") {
    assertNoDiff(
      compileErrors("""spec"proj:datasetid.tableid""""),
      """|error: Table reference is not in [project_id]:[dataset_id].[table_id] format: proj:datasetid.tableid
         |spec"proj:datasetid.tableid"
         |     ^
         |""".stripMargin
    )
  }

  test("spec without dataset") {
    assertNoDiff(
      compileErrors("""spec"projectid:tableid""""),
      """|error: Table reference is not in [project_id]:[dataset_id].[table_id] format: projectid:tableid
         |spec"projectid:tableid"
         |     ^
         |""".stripMargin
    )
  }

  test("spec without tableid") {
    assertNoDiff(
      compileErrors("""spec"projectid:dataset_id""""),
      """|error: Table reference is not in [project_id]:[dataset_id].[table_id] format: projectid:dataset_id
         |spec"projectid:dataset_id"
         |     ^
         |""".stripMargin
    )
  }

  test("spec complete format") {
    val s = spec"projectid:datasetid.tableid"

    assertEquals(s.ref.getProjectId(), "projectid")
    assertEquals(s.ref.getDatasetId(), "datasetid")
    assertEquals(s.ref.getTableId(), "tableid")
  }

  test("spec without project") {
    val s = spec"datasetid.tableid"

    assertEquals(s.ref.getProjectId(), null)
    assertEquals(s.ref.getDatasetId(), "datasetid")
    assertEquals(s.ref.getTableId(), "tableid")
  }
}
