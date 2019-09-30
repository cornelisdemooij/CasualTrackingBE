package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.service.DataPointService._

import scala.util.{Failure, Success}

object DataCollectionEndpoint extends CustomFormats {
  def route: Route = concat(
    get {
      pathPrefix("datacollection" / LongNumber) { id =>
        onComplete(getDataPointsInDataCollection(id)) {
          case Success(d) => complete(d)
          case Failure(e) => { println(e); complete(StatusCodes.NotFound) }
        }
      }
    }
  )
}
