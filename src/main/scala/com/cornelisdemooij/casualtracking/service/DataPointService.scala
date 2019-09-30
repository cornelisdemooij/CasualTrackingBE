package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.Entities.DataPoint
import com.cornelisdemooij.casualtracking.persistence.{DataPointRepository, DbConfiguration}

import scala.concurrent.Future

object DataPointService extends DbConfiguration {
  private val dataPointRepo = new DataPointRepository(config)
  dataPointRepo.init()

  def createDataPoint(dataPoint: DataPoint): Future[DataPoint] = dataPointRepo.insert(dataPoint)
  def getDataPoint(dataPointId: Long): Future[Option[DataPoint]] = dataPointRepo.find(dataPointId)
  def getDataPointsInDataCollection(dataCollectionId: Long): Future[List[DataPoint]] = dataPointRepo.findByDataCollectionId(dataCollectionId)
}
