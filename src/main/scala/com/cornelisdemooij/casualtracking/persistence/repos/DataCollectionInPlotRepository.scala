package com.cornelisdemooij.casualtracking.persistence.repos

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollectionInPlot
import com.cornelisdemooij.casualtracking.persistence.Db
import com.cornelisdemooij.casualtracking.persistence.tables.{DataCollectionTable, DataCollectionInPlotTable}
import slick.basic.DatabaseConfig
import slick.dbio.DBIOAction
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class DataCollectionInPlotRepository(val config: DatabaseConfig[JdbcProfile])
  extends Db with DataCollectionInPlotTable with DataCollectionTable {
  import config.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  def init(): Future[Unit] = db.run(DBIOAction.seq(dataCollectionInPlots.schema.create))
  def drop(): Future[Unit] = db.run(DBIOAction.seq(dataCollectionInPlots.schema.drop))

  def insert(dataCollectionInPlot: DataCollectionInPlot): Future[DataCollectionInPlot] = {
    db
      .run(dataCollectionInPlots returning dataCollectionInPlots.map(_.id) += dataCollectionInPlot)
      .map(id => dataCollectionInPlot.copy(id = Some(id)))
  }

  def find(id: Long): Future[Option[DataCollectionInPlot]] =
    db.run(dataCollectionInPlots.filter(_.id === id).result.headOption)
  def findByDataCollectionId(dataCollectionId: Long): Future[List[DataCollectionInPlot]] =
    db.run(dataCollections.join(dataCollectionInPlots).on(_.id === _.dataCollectionId).result.map(_.map(_._2).toList))
  def findByPlotId(plotId: Long): Future[List[DataCollectionInPlot]] =
    db.run(plots.join(dataCollectionInPlots).on(_.id === _.plotId).result.map(_.map(_._2).toList))

  def update(id: Long, dataCollectionId: Long, plotId: Long): Future[Boolean] = {
    val query = for (dcip <- dataCollectionInPlots if dcip.id === id)
      yield (dcip.dataCollectionId, dcip.plotId)
    db.run(query.update((dataCollectionId, plotId))) map { _ > 0 }
  }

  def delete(id: Long): Future[Boolean] =
    db.run(dataCollectionInPlots.filter(_.id === id).delete) map { _ > 0 }
}
