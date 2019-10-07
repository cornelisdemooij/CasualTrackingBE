package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.Entities.Plot
import com.cornelisdemooij.casualtracking.persistence.DbConfiguration
import com.cornelisdemooij.casualtracking.persistence.repos.{DataCollectionInPlotRepository, DataCollectionRepository, PlotRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PlotService extends DbConfiguration {
  private val plotRepo = new PlotRepository(config)
  plotRepo.init()
  private val dataCollectionInPlotRepo = new DataCollectionInPlotRepository(config)
  dataCollectionInPlotRepo.init()
  private val dataCollectionRepo = new DataCollectionRepository(config)
  dataCollectionRepo.init()

  def createPlot(plot: Plot): Future[Plot] = plotRepo.insert(plot)
  def getPlot(plotId: Long): Future[Plot] =
    for {
      emptyPlot <- plotRepo.find(plotId).flatMap(
        _.map(plot => Future.successful(plot))
          .getOrElse(Future.failed(new Exception(s"Plot $plotId not found!")))
      )
      dcids <- dataCollectionInPlotRepo.findByPlotId(plotId).map(_.map(_.dataCollectionId))
      dataCollections <- Future.traverse(dcids)(dcid => dataCollectionRepo.find(dcid))
    } yield emptyPlot.copy(dataCollectionList = dataCollections.flatten)
}
