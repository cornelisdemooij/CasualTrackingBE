package com.cornelisdemooij.casualtracking.persistence.tables

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollectionInPlot
import com.cornelisdemooij.casualtracking.persistence.{CustomMappers, Db}

trait DataCollectionInPlotTable extends DataCollectionTable with PlotTable with CustomMappers { this: Db =>
  import config.profile.api._

  class DataCollectionInPlots(tag: Tag) extends Table[DataCollectionInPlot](tag, "dataCollectionInPlots") {
    // Columns:
    def id = column[Long]("dataCollectionInPlot_id", O.PrimaryKey, O.AutoInc)
    
    // ForeignKey
    def dataCollectionId = column[Long]("dataCollection_id")
    def dataCollectionFk = foreignKey("dataCollection_fk", dataCollectionId, dataCollections)(_.id, ForeignKeyAction.Restrict, ForeignKeyAction.Cascade)
    def plotId = column[Long]("plot_id")
    def plotFk = foreignKey("plot_fk", plotId, plots)(_.id, ForeignKeyAction.Restrict, ForeignKeyAction.Cascade)

    // Select:
    def * = (id.?, dataCollectionId, plotId) <> (DataCollectionInPlot.tupled, DataCollectionInPlot.unapply)
  }

  val dataCollectionInPlots = TableQuery[DataCollectionInPlots]
}
