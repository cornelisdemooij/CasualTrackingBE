package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.DataPoint
import com.cornelisdemooij.casualtracking.persistence.{DataPointRepository, DbConfiguration}

import scala.concurrent.Future

object DataPointService extends DbConfiguration {
  private val dataPointRepo = new DataPointRepository(config)
  dataPointRepo.init()

  def createDataPoint(dataPoint: DataPoint): Future[DataPoint] = { println("in createDataPoint"); dataPointRepo.insert(dataPoint) }
  def getDataPoint(dataPointId: Long): Future[Option[DataPoint]] = dataPointRepo.find(dataPointId)
}
