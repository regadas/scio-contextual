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

object pubsub {
  private[this] val ProjectIdRegExp =
    "[a-z][-a-z0-9:.]{4,61}[a-z0-9]".r
  private[this] val PubSubNameRegExp =
    "[a-zA-Z][-._~%+a-zA-Z0-9]{2,254}+".r
  private[this] val V1Beta1RegExp =
    "/(topics|subscriptions)/([^/]+)/(.+)".r
  private[this] val DefaultRegExp =
    "projects/([^/]+)/(topics|subscriptions)/(.+)".r

  def validateProjectId(name: String): Either[String, String] =
    Either.cond(
      name.matches(ProjectIdRegExp.regex),
      name,
      s"Illegal project name: needs to be $ProjectIdRegExp"
    )

  def validatePubSubName(name: String): Either[String, String] =
    Either.cond(
      name.matches(PubSubNameRegExp.regex),
      name,
      s"Invalid Pubsub name: name needs to be $PubSubNameRegExp"
    )

  def validate(name: String, tpe: String): Either[String, String] = {
    val parsed = name match {
      case V1Beta1RegExp(t, projectName, topicName) if t == tpe =>
        Right((projectName, topicName))
      case DefaultRegExp(projectName, t, topicName) if t == tpe =>
        Right((projectName, topicName))
      case _ =>
        Left(
          s"invalid format: projects/<project_id>/$tpe/<name>"
        )
    }

    for {
      p <- parsed
      pn <- validateProjectId(p._1)
      tn <- validatePubSubName(p._2)
    } yield name
  }

  object TopicParser extends Verifier[String] {
    override def check(string: String): Either[(Int, String), String] = {
      validate(string, "topics").left.map(m => (0, m))
    }
  }

  object SubscriptionParser extends Verifier[String] {
    override def check(string: String): Either[(Int, String), String] = {
      validate(string, "subscriptions").left.map(m => (0, m))
    }
  }

  implicit class PubsubContext(sc: StringContext) {
    val topic = Prefix(TopicParser, sc)
    val subscription = Prefix(SubscriptionParser, sc)
  }
}
