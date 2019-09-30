package com.cornelisdemooij.casualtracking.persistence

import com.cornelisdemooij.casualtracking.domain.Entities.DataPoint
import org.joda.time.DateTime

trait DataPointTable extends DataCollectionTable with CustomMappers { this: Db =>
  import config.profile.api._

  class DataPoints(tag: Tag) extends Table[DataPoint](tag, "dataPoints") {
    // Columns:
    def id = column[Long]("dataPoint_id", O.PrimaryKey, O.AutoInc)
    def value = column[Double]("value")
    def moment = column[DateTime]("moment")
    def note = column[Option[String]]("note")

    // ForeignKey
    def dataCollectionId = column[Long]("dataCollection_id")
    def dataCollectionFk = foreignKey("dataCollection_fk", dataCollectionId, dataCollections)(_.id, ForeignKeyAction.Restrict, ForeignKeyAction.Cascade)

    // Select:
    def * = (id.?, dataCollectionId, value, moment, note) <> (DataPoint.tupled, DataPoint.unapply)
  }

  val dataPoints = TableQuery[DataPoints]
}
