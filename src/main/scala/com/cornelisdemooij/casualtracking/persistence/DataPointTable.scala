package com.cornelisdemooij.casualtracking.persistence

import java.sql.Timestamp

import com.cornelisdemooij.casualtracking.domain.DataPoint
import org.joda.time.DateTime

trait DataPointTable { this: Db =>
  import config.profile.api._

  implicit def dateTimeMapper: BaseColumnType[DateTime] =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime)
    )

  class DataPoints(tag: Tag) extends Table[DataPoint](tag, "dataPoints") {
    println("in DataPoints")

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
