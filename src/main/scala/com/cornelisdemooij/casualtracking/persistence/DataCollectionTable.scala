package com.cornelisdemooij.casualtracking.persistence

import com.cornelisdemooij.casualtracking.domain.DataCollection
import com.cornelisdemooij.casualtracking.domain.DataPoint
import org.joda.time.DateTime

trait DataCollectionTable extends CustomMappers { this: Db =>
  import config.profile.api._

  val dataPoints = TableQuery[DataPointTable]
  val dataCollections = TableQuery[DataCollections]

  class DataCollections(tag: Tag) extends Table[DataCollection](tag, "dataCollections") {
    // Columns:
    def id = column[Long]("dataCollection_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def createdOn = column[DateTime]("createdOn")
    def dataPointList = dataCollections.join(dataPoints).on(_.id === _.)

    // Select:
    def * = (id.?, name, createdOn, dataPointList) <> (DataCollection.tupled, DataCollection.unapply)
  }

}
