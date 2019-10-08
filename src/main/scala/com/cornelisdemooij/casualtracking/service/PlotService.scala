package com.cornelisdemooij.casualtracking.service

import com.cornelisdemooij.casualtracking.domain.Entities.Plot
import com.cornelisdemooij.casualtracking.persistence.DbConfiguration
import com.cornelisdemooij.casualtracking.persistence.repos.PlotRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PlotService extends DbConfiguration {
  private val plotRepo = new PlotRepository(config)
  plotRepo.init()

  def createPlot(plot: Plot): Future[Plot] = plotRepo.insert(plot)
  def getPlot(plotId: Long): Future[Plot] =
    for {
      emptyPlot <- plotRepo.find(plotId).flatMap(
        _.map(plot => Future.successful(plot))
          .getOrElse(Future.failed(new Exception(s"Plot $plotId not found!")))
      )
      dcids <- DataCollectionInPlotService.getDataCollectionIdsForPlotId(plotId)
      dataCollections <- Future.traverse(dcids)(dcid => DataCollectionService.getDataCollection(dcid))
    } yield emptyPlot.copy(dataCollectionList = dataCollections)
}
