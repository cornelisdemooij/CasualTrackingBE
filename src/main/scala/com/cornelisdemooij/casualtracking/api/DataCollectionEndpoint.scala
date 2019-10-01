package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.service.DataCollectionService._
import com.cornelisdemooij.casualtracking.service.DataPointService._

import scala.util.{Failure, Success}

object DataCollectionEndpoint extends CustomFormats {
  def route: Route = concat(
    post {
      path("datacollection") {
        println("in post")
        entity(as[DataCollection]) { dataCollection =>
          onComplete(createDataCollection(dataCollection)) {
            case Success(d) => complete(StatusCodes.Created, s"DataCollection ${d.id} created!")
            case Failure(e) => println(e); complete(StatusCodes.InternalServerError, "Creating dataCollection failed!")
          }
        }
      }
    },
    get {
      pathPrefix("datacollection" / LongNumber) { id =>
        onSuccess(getDataCollection(id)) {
          case Some(dataCollection) => complete(dataCollection)
          case None                 => complete(StatusCodes.NotFound)
        }
      }
    },
    get {
      println("0")
      pathPrefix("datacollection" / LongNumber) { id =>
        onComplete(getDataPointsInDataCollection(id)) {
          case Success(d) => complete(d)
          case Failure(e) => println(e); complete(StatusCodes.NotFound)
        }
      }
    }
  )
}
