package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.persistence.DbConfiguration
import com.cornelisdemooij.casualtracking.persistence.repos.DataCollectionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DataCollectionService extends DbConfiguration {
  private val dataCollectionRepo = new DataCollectionRepository(config)
  dataCollectionRepo.init()
  
  def createDataCollection(dataCollection: DataCollection): Future[DataCollection] =
    dataCollectionRepo.insert(dataCollection)
  def getDataCollection(dataCollectionId: Long): Future[DataCollection] =
    dataCollectionRepo.find(dataCollectionId).flatMap {
      case Some(dc) => Future.successful(dc)
      case None => Future.failed(new Exception(s"Data collection $dataCollectionId not found!"))
    }
}
