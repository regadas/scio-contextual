package io.regadas.scio.contextual

import contextual._
import scala.util.matching._
import com.spotify.scio.bigquery.Source
import com.spotify.scio.bigquery.Table
import scala.util.Try
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.json.jackson2.JacksonFactory
import java.io.StringReader
import com.google.api.services.bigquery.model.TableReference
import scala.annotation.compileTimeOnly

object bigquery {
  object TableSpecParser extends Interpolator {
    override type Output = Table.Spec

    override def contextualize(
        interpolation: StaticInterpolation
    ): Seq[ContextType] = {
      val lit @ Literal(_, specString) = interpolation.parts.head
      Try(Table.Spec(specString).ref)
        .fold(t => interpolation.abort(lit, 0, t.getMessage), _ => Nil)
    }

    def evaluate(interpolation: RuntimeInterpolation): Table.Spec =
      Table.Spec(interpolation.literals.head)
  }

  object TableRefParser extends Interpolator {
    private[this] val Parser = new JsonObjectParser(new JacksonFactory)

    override type Output = Table.Ref

    override def contextualize(
        interpolation: StaticInterpolation
    ): Seq[ContextType] = {
      val lit @ Literal(_, ref) = interpolation.parts.head
      Try {
        Parser.parseAndClose(new StringReader(ref), classOf[TableReference])
      }.map { tr =>
          if (tr.getProjectId() == null) {
            interpolation.abort(lit, 0, "project id is null")
          }
          if (tr.getDatasetId() == null) {
            interpolation.abort(lit, 0, "dataset id is null")
          }
          if (tr.getTableId() == null) {
            interpolation.abort(lit, 0, "table id is null")
          }

          Table.Spec(Table.Ref(tr).spec).ref
        }
        .fold(
          t => interpolation.abort(lit, 0, s"invalid json: ${t.getMessage}"),
          _ => Nil
        )
    }

    def evaluate(interpolation: RuntimeInterpolation): Table.Ref = {
      val tr = Parser.parseAndClose(
        new StringReader(interpolation.literals.head),
        classOf[TableReference]
      )
      Table.Ref(tr)
    }
  }

  implicit class BigQueryContext(sc: StringContext) {
    val ref = Prefix(TableRefParser, sc)
    val spec = Prefix(TableSpecParser, sc)
  }

}
