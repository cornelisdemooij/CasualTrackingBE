package com.cornelisdemooij.casualtracking.persistence.repos

import com.cornelisdemooij.casualtracking.domain.Entities.Plot
import com.cornelisdemooij.casualtracking.persistence.Db
import com.cornelisdemooij.casualtracking.persistence.tables.PlotTable
import org.joda.time.DateTime
import slick.basic.DatabaseConfig
import slick.dbio.DBIOAction
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class PlotRepository(val config: DatabaseConfig[JdbcProfile])
  extends Db with PlotTable {
  import config.profile.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  def init(): Future[Unit] = db.run(DBIOAction.seq(plots.schema.create))
  def drop(): Future[Unit] = db.run(DBIOAction.seq(plots.schema.drop))

  def insert(plot: Plot): Future[Plot] = {
    db
      .run(plots returning plots.map(_.id) += plot)
      .map(id => plot.copy(id = Some(id)))
  }

  def find(id: Long): Future[Option[Plot]] =
    db.run(plots.filter(_.id === id).result.headOption)

  def update(id: Long, title: String, createdOn: DateTime): Future[Boolean] = {
    val query = for (plot <- plots if plot.id === id)
      yield (plot.title, plot.createdOn)
    db.run(query.update((title, createdOn))) map { _ > 0 }
  }

  def delete(id: Long): Future[Boolean] =
    db.run(plots.filter(_.id === id).delete) map { _ > 0 }
}
