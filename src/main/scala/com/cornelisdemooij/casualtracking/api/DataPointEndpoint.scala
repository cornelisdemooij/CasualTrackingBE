package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.cornelisdemooij.casualtracking.domain.DataPoint
import com.cornelisdemooij.casualtracking.service.DataPointService._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

import scala.util.{Failure, Success}

object DataPointEndpoint {
  implicit object dateTimeFormat extends RootJsonFormat[DateTime] {
    private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
    override def write(obj: DateTime) = JsString(parserISO.print(obj))
    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parserISO.parseDateTime(s)
      case _ => throw DeserializationException("Error parsing string to date and time. Should be in ISO format.")
    }
  }
  implicit val dataPointFormat: RootJsonFormat[DataPoint] = jsonFormat4(DataPoint)

  def route: Route = concat(
    post {
      path("datapoint") {
        println("in post")
        entity(as[DataPoint]) { dataPoint =>
          onComplete(createDataPoint(dataPoint)) {
            case Success(d) => complete(StatusCodes.Created, s"DataPoint ${d.id} created!")
            case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating datapoint failed!")
          }
        }
      }
    },
    get {
      pathPrefix("datapoint" / LongNumber) { id =>
        onSuccess(getDataPoint(id)) {
          case Some(dataPoint) => complete(dataPoint)
          case None            => complete(StatusCodes.NotFound)
        }
      }
    }
  )
}
