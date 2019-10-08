package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.service.DataCollectionService._

import scala.util.{Failure, Success}

object DataCollectionEndpoint extends CustomFormats with CORSHandler {
  def route: Route = concat(
    post {
      corsHandler(
        path("datacollection") {
          entity(as[DataCollection]) { dataCollection =>
            onComplete(createDataCollection(dataCollection)) {
              case Success(d) => complete(StatusCodes.Created, s"Data collection ${d.id} created!")
              case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating data collection failed!")
            }
          }
        }
      )
    },
    get {
      corsHandler(
        pathPrefix("datacollection" / LongNumber) { id =>
          onComplete(getDataCollection(id)) {
            case Success(d) => complete(d)
            case Failure(e) => println(e); complete(StatusCodes.NotFound)
          }
        }
      )
    }
  )
}
