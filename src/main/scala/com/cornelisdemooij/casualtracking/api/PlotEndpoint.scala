package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.domain.Entities.Plot
import com.cornelisdemooij.casualtracking.service.PlotService._

import scala.util.{Failure, Success}

object PlotEndpoint extends CustomFormats with CORSHandler {
  def route: Route = concat(
    post {
      corsHandler(
        path("plot") {
          entity(as[Plot]) { plot =>
            onComplete(createPlot(plot)) {
              case Success(d) => complete(StatusCodes.Created, s"Plot ${d.id} created!")
              case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating plot failed!")
            }
          }
        }
      )
    },
    get {
      corsHandler(
        pathPrefix("plot" / LongNumber) { id =>
          val collectionFuture = getPlot(id)
          onComplete(collectionFuture) {
            case Success(p) => complete(p)
            case Failure(e) => println(e); complete(StatusCodes.NotFound)
          }
        }
      )
    }
  )
}
