package com.cornelisdemooij.casualtracking.persistence.tables

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.persistence.{CustomMappers, Db}
import org.joda.time.DateTime

trait DataCollectionTable extends CustomMappers { this: Db =>
  import config.profile.api._

  class DataCollections(tag: Tag) extends Table[DataCollection](tag, "dataCollections") {
    private type Data = (Option[Long], String, DateTime)

    def constructDataCollection: Data => DataCollection = {
      case (id, name, createdOn) => DataCollection(id, name, createdOn, List.empty)
    }
    def extractDataCollection: PartialFunction[DataCollection, Data] = {
      case DataCollection(id, name, createdOn, _) => (id, name, createdOn)
    }

    // Columns:
    def id = column[Long]("dataCollection_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def createdOn = column[DateTime]("createdOn")

    // Select:
    def * = (id.?, name, createdOn) <> (constructDataCollection, extractDataCollection.lift)
  }

  val dataCollections = TableQuery[DataCollections]
}
