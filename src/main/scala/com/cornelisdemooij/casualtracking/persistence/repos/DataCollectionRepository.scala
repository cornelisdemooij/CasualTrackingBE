package com.cornelisdemooij.casualtracking.persistence.repos

import com.cornelisdemooij.casualtracking.domain.Entities.DataCollection
import com.cornelisdemooij.casualtracking.persistence.Db
import com.cornelisdemooij.casualtracking.persistence.tables.DataCollectionTable
import org.joda.time.DateTime
import slick.basic.DatabaseConfig
import slick.dbio.DBIOAction
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class DataCollectionRepository(val config: DatabaseConfig[JdbcProfile])
      extends Db with DataCollectionTable {
  import config.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  def init(): Future[Unit] = db.run(DBIOAction.seq(dataCollections.schema.create))
  def drop(): Future[Unit] = db.run(DBIOAction.seq(dataCollections.schema.drop))

  def insert(dataCollection: DataCollection): Future[DataCollection] = {
    db
      .run(dataCollections returning dataCollections.map(_.id) += dataCollection)
      .map(id => dataCollection.copy(id = Some(id)))
  }

  def find(id: Long): Future[Option[DataCollection]] =
    db.run(dataCollections.filter(_.id === id).result.headOption)

  def update(id: Long, name: String, createdOn: DateTime): Future[Boolean] = {
    val query = for (dataCollection <- dataCollections if dataCollection.id === id)
      yield (dataCollection.name, dataCollection.createdOn)
    db.run(query.update((name, createdOn))) map { _ > 0 }
  }

  def delete(id: Long): Future[Boolean] =
    db.run(dataCollections.filter(_.id === id).delete) map { _ > 0 }
}
