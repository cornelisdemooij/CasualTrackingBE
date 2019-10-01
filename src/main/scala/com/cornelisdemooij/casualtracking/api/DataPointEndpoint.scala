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
              case Success(d) => complete(StatusCodes.Created, s"DataPoint ${d.id} created!")
              case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating datapoint failed!")
            }
          }
        }
      )
    },
    get {
      corsHandler(
        pathPrefix("datapoint" / LongNumber) { id =>
          onSuccess(getDataPoint(id)) {
            case Some(dataPoint) => complete(dataPoint)
            case None            => complete(StatusCodes.NotFound)
          }
        }
      )
    }
  )
}
