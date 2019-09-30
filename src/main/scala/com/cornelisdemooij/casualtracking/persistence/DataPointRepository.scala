package com.cornelisdemooij.casualtracking.persistence

import com.cornelisdemooij.casualtracking.domain.DataPoint
import org.joda.time.DateTime
import slick.basic.DatabaseConfig
import slick.dbio.DBIOAction
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class DataPointRepository(val config: DatabaseConfig[JdbcProfile])
      extends Db with DataPointTable {
  import config.profile.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  def init(): Future[Unit] = db.run(DBIOAction.seq(dataPoints.schema.create))
  def drop(): Future[Unit] = db.run(DBIOAction.seq(dataPoints.schema.drop))

  def insert(dataPoint: DataPoint): Future[DataPoint] = {
    db
      .run(dataPoints returning dataPoints.map(_.id) += dataPoint)
      .map(id => dataPoint.copy(id = Some(id)))
  }

  def find(id: Long): Future[Option[DataPoint]] =
    db.run(dataPoints.filter(_.id === id).result.headOption)
  def findByDataCollectionId(dataCollectionId: Long): Future[Option[List[DataPoint]]] =
    db.run(dataPoints.filter(_.dataCollectionId === dataCollectionId).result)

  def update(id: Long, value: Double, moment: DateTime, note: String): Future[Boolean] = {
    val query = for (dataPoint <- dataPoints if dataPoint.id === id)
      yield (dataPoint.value, dataPoint.moment, dataPoint.note)
    db.run(query.update((value, moment, Some(note)))) map { _ > 0 }
  }

  def delete(id: Long): Future[Boolean] =
    db.run(dataPoints.filter(_.id === id).delete) map { _ > 0 }
}
