package com.cornelisdemooij.casualtracking.persistence.tables

import com.cornelisdemooij.casualtracking.domain.Entities.Plot
import com.cornelisdemooij.casualtracking.persistence.{CustomMappers, Db}
import org.joda.time.DateTime

trait PlotTable extends CustomMappers { this: Db =>
  import config.profile.api._

  class Plots(tag: Tag) extends Table[Plot](tag, "plots") {
    private type Data = (Option[Long], String, DateTime)

    def constructPlot: Data => Plot = {
      case (id, title, createdOn) => Plot(id, title, createdOn, List.empty)
    }
    def extractPlot: PartialFunction[Plot, Data] = {
      case Plot(id, title, createdOn, _) => (id, title, createdOn)
    }

    // Columns:
    def id = column[Long]("plot_id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def createdOn = column[DateTime]("createdOn")

    // Select:
    def * = (id.?, title, createdOn) <> (constructPlot, extractPlot.lift)
  }

  val plots = TableQuery[Plots]
}
