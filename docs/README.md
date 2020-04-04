# scio-contextual ![](https://github.com/regadas/scio-contextual/workflows/ci/badge.svg)

Small library that adds some potencially useful string interpolators to use in your [scio](https://github.com/spotify/scio) pipeline when you need to have things interpreted at compile-time.

```scala
libraryDependencies ++= Seq(
  "io.regadas" %% "scio-contextual" % "<version>"
)
```

## BigQuery

```scala mdoc
import io.regadas.scio.contextual.bigquery._
```

### Valid

```scala mdoc
spec"projectid:datasetid.tableid"

ref"""
{
  "datasetId": "asd",
  "projectId":  "asdasd",
  "tableId": "asd"  
}
"""
```

### Invalid

```scala mdoc:fail
// project id needs to be at least 6 chars
spec"proj:datasetid.tableid"
```

## Pubsub

```scala mdoc
import io.regadas.scio.contextual.pubsub._
```

### Valid

```scala mdoc
subscription"projects/projectId/subscriptions/subName"

topic"projects/projectId/topics/name"
```

### Invalid

```scala mdoc:fail
// invalid project id

subscription"projects/proj/subscriptions/subName"

topic"projects/proj/topics/name"
```
