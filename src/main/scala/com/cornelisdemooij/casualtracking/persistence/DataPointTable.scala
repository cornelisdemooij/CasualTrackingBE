package com.cornelisdemooij.casualtracking.persistence

import akka.http.scaladsl.model.DateTime
import com.cornelisdemooij.casualtracking.domain.DataPoint

trait DataPointTable { this: Db =>
  import config.profile.api._

  class DataPoints(tag: Tag) extends Table[DataPoint](tag, "dataPoints") {
    // Columns:
    def id = column[Long]("dataPoint_id", O.PrimaryKey, O.AutoInc)
    def value = column[Double]("value")
    def moment = column[DateTime]("moment")
    def note = column[Option[String]]("note")

    // Select:
    def * = (id.?, value, moment, note) <> (DataPoint.tupled, DataPoint.unapply)
  }

  val dataPoints = TableQuery[DataPoints]
}
