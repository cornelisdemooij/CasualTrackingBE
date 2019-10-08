package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.domain.Entities.DataPoint
import com.cornelisdemooij.casualtracking.service.DataPointService._

import scala.util.{Failure, Success}

object DataPointEndpoint extends CustomFormats with CORSHandler {
  def route: Route = concat(
    post {
      corsHandler (
        path("datapoint") {
          entity(as[DataPoint]) { dataPoint =>
            onComplete(createDataPoint(dataPoint)) {
              case Success(d) => complete(StatusCodes.Created, s"Data point ${d.id} created!")
              case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating data point failed!")
            }
          }
        }
      )
    },
    get {
      corsHandler(
        pathPrefix("datapoint" / LongNumber) { id =>
          onComplete(getDataPoint(id)) {
            case Success(d) => complete(d)
            case Failure(e) => println(e); complete(StatusCodes.NotFound)
          }
        }
      )
    }
  )
}
