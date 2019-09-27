package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.cornelisdemooij.casualtracking.domain.DataPoint
import com.cornelisdemooij.casualtracking.service.DataPointService._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.util.{Failure, Success}

object DataPointEndpoint {
  implicit val dataPointFormat: RootJsonFormat[DataPoint] = jsonFormat4(DataPoint)

  def route: Route = concat(
    post {
      path("datapoint") {
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
