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

## Google BigQuery

```scala mdoc
import io.regadas.scio.contextual.bigquery._
```

### Valid

```scala mdoc
spec"projectid:datasetid.tableid"

ref"""
{
  "datasetId": "dataset",
  "projectId":  "project",
  "tableId": "table"  
}
"""
```

### Invalid

```scala mdoc:fail
// project id needs to be at least 6 chars
spec"proj:datasetid.tableid"
```

## Google Cloud Pub/Sub

```scala mdoc
import io.regadas.scio.contextual.pubsub._
```

### Valid

```scala mdoc
subscription"projects/project-id/subscriptions/subName"

topic"projects/project-id/topics/name"
```

### Invalid

```scala mdoc:fail
// invalid project id

subscription"projects/proj/subscriptions/subName"

topic"projects/proj/topics/name"
```

## Google Cloud Storage

```scala mdoc
import io.regadas.scio.contextual.gcs._
```

### Valid

```scala mdoc
gcs"gs://bucket/scio-contextual"
```

### Invalid

```scala mdoc:fail
// invalid bucket
gcs"gs://bucket_/scio-contextual"

gcs"gs://bu/scio-contextual"

// wrong schema
gcs"gcs://bucket/scio-contextual"
```
