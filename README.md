# scio-contextual ![](https://github.com/regadas/scio-contextual/workflows/ci/badge.svg)

Small library that adds some potencially useful string interpolators to use in your [scio](https://github.com/spotify/scio) pipeline when you need to have things interpreted at compile-time.

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
  "datasetId": "asd",
  "projectId":  "asdasd",
  "tableId": "asd"  
}
"""
// res1: com.spotify.scio.bigquery.Table.Ref = Ref(
//   {"datasetId":"asd","projectId":"asdasd","tableId":"asd"}
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
