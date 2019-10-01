package com.cornelisdemooij.casualtracking.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.service.DataCollectionService._
import com.cornelisdemooij.casualtracking.service.DataPointService._
import scala.concurrent.ExecutionContext.Implicits.global

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
        val collectionFuture = getDataCollection(id)
        val dataPointsFuture = getDataPointsInDataCollection(id)
        val combinedFuture =
          for {
            collection <- collectionFuture
            dataPoints <- dataPointsFuture
          } yield collection.map(_.copy(dataPointList = Some(dataPoints)))

        onComplete(combinedFuture) {
          case Success(dataCollection) => complete(dataCollection)
          case Failure(e)              => println(e); complete(StatusCodes.NotFound)
        }
      }
    }
  )
}
