package com.cornelisdemooij.casualtracking.persistence

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import org.joda.time.DateTime

trait DataCollectionTable extends CustomMappers { this: Db =>
  import config.profile.api._

  class DataCollections(tag: Tag) extends Table[DataCollection](tag, "dataCollections") {
    // Columns:
    def id = column[Long]("dataCollection_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def createdOn = column[DateTime]("createdOn")
    //val dataPointListQuery = dataCollections.join(dataPoints)
    //def dataPointList = dataCollections.join(dataPoints).on(_.id === _.)

    // Select:
    def * = (id.?, name, createdOn) <> (DataCollection.tupled, DataCollection.unapply)
  }

  val dataCollections = TableQuery[DataCollections]
}
