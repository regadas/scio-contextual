# scio-contextual

![build](https://github.com/regadas/scio-contextual/workflows/main/badge.svg)
[![GitHub license](https://img.shields.io/github/license/regadas/scio-contextual.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.regadas/scio-contextual_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/io.regadas/scio-contextual_2.12)

Small library that adds some potencially useful string interpolators to use in your [scio](https://github.com/spotify/scio) pipeline when you need to have things interpreted at compile-time.

```scala
libraryDependencies ++= Seq(
  "io.regadas" %% "scio-contextual" % "<version>"
)
```

## BigQuery

```scala
import io.regadas.scio.contextual.bigquery._
```

### Valid

```scala
spec"projectid:datasetid.tableid"
// res0: com.spotify.scio.bigquery.Table.Spec = Spec(
//   "projectid:datasetid.tableid"
// )

ref"""
{
  "datasetId": "dataset",
  "projectId":  "project",
  "tableId": "table"  
}
"""
// res1: com.spotify.scio.bigquery.Table.Ref = Ref(
//   {"datasetId":"dataset","projectId":"project","tableId":"table"}
// )
```

### Invalid

```scala
// project id needs to be at least 6 chars
spec"proj:datasetid.tableid"
// error: Table reference is not in [project_id]:[dataset_id].[table_id] format: proj:datasetid.tableid
// spec"proj:datasetid.tableid"
//      ^^^^^^^^^^^^^^^^^^^^^^^
```

## Pubsub

```scala
import io.regadas.scio.contextual.pubsub._
```

### Valid

```scala
subscription"projects/projectId/subscriptions/subName"
// res3: String = "projects/projectId/subscriptions/subName"

topic"projects/projectId/topics/name"
// res4: String = "projects/projectId/topics/name"
```

### Invalid

```scala
// invalid project id

subscription"projects/proj/subscriptions/subName"

topic"projects/proj/topics/name"
// error: Illegal project name: needs to be [a-z][-a-z0-9:.]{4,61}[a-z0-9]
// subscription"projects/proj/subscriptions/subName"
//              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// error: Illegal project name: needs to be [a-z][-a-z0-9:.]{4,61}[a-z0-9]
// topic"projects/proj/topics/name"
//       ^^^^^^^^^^^^^^^^^^^^^^^^^^
```
