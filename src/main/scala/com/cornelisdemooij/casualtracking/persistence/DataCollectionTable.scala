package com.cornelisdemooij.casualtracking.persistence

import com.cornelisdemooij.casualtracking.domain.DataCollection
import org.joda.time.DateTime

import com.cornelisdemooij.casualtracking.persistence.DataPointTable

trait DataCollectionTable extends CustomMappers { this: Db =>
  import config.profile.api._

  class DataCollections(tag: Tag) extends Table[DataCollection](tag, "dataCollections") {
    // Columns:
    def id = column[Long]("dataCollection_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def createdOn = column[DateTime]("createdOn")
    //def dataPoints = Query(DataPoints).join(dataPoints).on() //List.empty //column[Option[String]]("note")

    // Select:
    def * = (id.?, name, createdOn, dataPoints) <> (DataCollection.tupled, DataCollection.unapply)
  }

  val dataCollections = TableQuery[DataCollections]
}
