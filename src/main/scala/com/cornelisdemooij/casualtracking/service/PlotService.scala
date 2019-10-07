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
  def getPlot(plotId: Long): Future[Plot] = {
    val emptyPlotOptionFuture = plotRepo.find(plotId)
    val dcipsFuture = dataCollectionInPlotRepo.findByPlotId(plotId)

    val dcidsFuture = for {
        dcips <- dcipsFuture
      } yield dcips.map(_.dataCollectionId)

    val dcsFuture =
      for {
        dcids <- dcidsFuture
        dataCollections <- Future.traverse(dcids)(dcid => dataCollectionRepo.find(dcid))
      } yield dataCollections.flatten

    val fullPlotFuture =
      for {
        emptyPlot <- emptyPlotOptionFuture flatMap (
          _.map(plot => Future.successful(plot))
            .getOrElse(Future.failed(new Exception("Plot not found!")))
        )
        dcs <- dcsFuture
      } yield emptyPlot.copy(dataCollectionList = dcs)

    fullPlotFuture
  }
}
