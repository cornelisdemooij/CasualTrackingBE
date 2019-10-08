package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollectionInPlot
import com.cornelisdemooij.casualtracking.persistence.DbConfiguration
import com.cornelisdemooij.casualtracking.persistence.repos.DataCollectionInPlotRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DataCollectionInPlotService extends DbConfiguration {
  private val dataCollectionInPlotRepo = new DataCollectionInPlotRepository(config)
  dataCollectionInPlotRepo.init()

  def createDataCollectionInPlot(dcip: DataCollectionInPlot): Future[DataCollectionInPlot] =
    dataCollectionInPlotRepo.insert(dcip)
  def getDataCollectionInPlot(dcipId: Long): Future[DataCollectionInPlot] =
    dataCollectionInPlotRepo.find(dcipId).flatMap {
      case Some(dcip) => Future.successful(dcip)
      case None       => Future.failed(new Exception(s"Data-collection-in-plot relationship $dcipId not found!"))
    }
  def getDataCollectionIdsForPlotId(plotId: Long): Future[List[Long]] =
    dataCollectionInPlotRepo.findByPlotId(plotId).map(_.map(_.dataCollectionId))
  def getPlotIdsForDataCollectionId(dcId: Long): Future[List[Long]] =
    dataCollectionInPlotRepo.findByDataCollectionId(dcId).map(_.map(_.plotId))
}
